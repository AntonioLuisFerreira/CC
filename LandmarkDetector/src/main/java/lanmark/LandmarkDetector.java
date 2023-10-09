package lanmark;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.*;
import com.google.common.io.ByteStreams;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.SubscriptionName;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class LandmarkDetector {

    final static int ZOOM = 15; // Streets
    final static String SIZE = "600x300";

    private final static String BUCKET_NAME = "16-europa-16";
    private final static String BUCKET_DESTINATION = "16-asia-16";
    private static final String PROJECT_ID = "cn2223-t2-g10";

    private final static String APIVISIONKEY = "AIzaSyBr7pkOzt1aR7NZ1TjEg6FZI8cgrIgyxj0";

    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
    StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();


    private static HashMap<String,String> dados;


    public LandmarkDetector() throws IOException {
        dados = new HashMap<>();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        LandmarkDetector landmarkDetector = new LandmarkDetector();
        landmarkDetector.startMessageReceiver("projecto-stub",APIVISIONKEY);
        /*landmarkDetector.detectLandmarksGcs(args[0]);*/
    }

    public void startMessageReceiver(String subscriptionId, String apiKey) throws IOException {
        SubscriptionName subscriptionName = SubscriptionName.of(PROJECT_ID, subscriptionId);

        MessageReceiver receiver = new MessageReceiver() {
            @Override
            public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
                String blobName = message.getData().toStringUtf8();
                try {
                    detectLandmarksGcs(blobName);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                consumer.ack();
            }
        };

        Subscriber subscriber = Subscriber.newBuilder(String.valueOf(subscriptionName), receiver)
                .setCredentialsProvider(() -> credentials)
                .build();
        subscriber.startAsync().awaitRunning();

        // Keep the main thread alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Detects landmarks in the specified remote image on Google Cloud Storage.
    public static void detectLandmarksGcs(String blobName) throws IOException, ExecutionException, InterruptedException {
        String blobGsPath = "gs://" + BUCKET_NAME + "/" + blobName;

        //keys do hash map
        String description;
        float score;
        double latitude;
        double longitude;

        dados.put("BlobName",blobName);


        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(blobGsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    return ;
                }

                boolean first = true;
                for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
                    LocationInfo info = annotation.getLocationsList().listIterator().next();
                    description = annotation.getDescription();
                    if (dados.get("description") == null) {
                        dados.put("description", description);
                    }
                    score = annotation.getScore();
                    Float currentScore = Float.parseFloat(dados.getOrDefault("score", "0"));
                    if (score > currentScore) {
                        dados.put("score", Float.toString(score));
                    }
                    latitude = info.getLatLng().getLatitude();
                    if (dados.get("latitude") == null) {
                        dados.put("latitude", Double.toString(latitude));
                    }
                    longitude = info.getLatLng().getLongitude();
                    if (dados.get("longitude") == null) {
                        dados.put("longitude", Double.toString(longitude));
                    }
                }
            }

            getStaticMapSaveImage(dados);
            storeDetectedData(dados);
            //reset do hashmap
            resetDados();

        }
    }

    private static HashMap<String, String> getDados() {
        return dados;
    }

    private static void resetDados() {
        dados.clear();
    }

    private static void printDadosCalculados(HashMap<String,String> dados){
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ": " + value);
        }
    }

    private static void getStaticMapSaveImage(HashMap<String, String> dados) throws IOException {
        double lat = Double.parseDouble(dados.get("latitude"));
        double log = Double.parseDouble(dados.get("longitude"));
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?"
                + "center=" + lat + "," + log
                + "&zoom=" + ZOOM
                + "&size=" + SIZE
                + "&key=" + APIVISIONKEY;
        generateImageAndSave(mapUrl);
    }

    private static void generateImageAndSave(String mapUrl) throws IOException {
        URL url = new URL(mapUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream in = conn.getInputStream();
        byte[] imageBytes = ByteStreams.toByteArray(in);
        in.close();

        //coloca no storage a imagem gerada
        putInStorage(imageBytes);
    }

    //init firestore
    private static Firestore initializeFirestore() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        return options.getService();
    }

    //colocar os dados na firestore, dps dar clear ao hashmap
    private static void storeDetectedData(HashMap<String, String> dados) throws IOException, InterruptedException,ExecutionException {
        Firestore firestore = initializeFirestore();

        DocumentReference docRef = firestore.collection("Dados-Imagens").document(dados.get("BlobName"));

        // Store the detection data in Firestore
        ApiFuture<WriteResult> future = docRef.set(dados);
        // Wait for the write operation to complete
        future.get();

        System.out.println("Detection data stored in Firestore: " + docRef.getId());
    }

    //guardar a imagem no bucket 16-asia-16
    public static void putInStorage(byte[] imageBytes) throws IOException {

        UUID imageId = UUID.randomUUID();

        String blobName = "blob-" + imageId.toString();
        dados.put("static-name",blobName);
        // Create the blob in the bucket using the byte stream
        BlobId blobId = BlobId.of(BUCKET_DESTINATION, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/jpeg")
                .build();

        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();
        Storage storage = storageOptions.getService();
        storage.create(blobInfo, imageBytes);
    }

}
