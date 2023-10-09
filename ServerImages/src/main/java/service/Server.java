package service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.ByteString;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class Server extends FileServiceGrpc.FileServiceImplBase {

    private static int svcPort;
    private static final String PROJECT_ID = "cn2223-t2-g10";

    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
    StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();

    public Server() throws IOException {
    }


    public static void main(String[] args) {
        svcPort = 7500;
        try {
            io.grpc.Server svc = ServerBuilder.forPort(svcPort).addService(new Server()).build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);
            Scanner scan = new Scanner(System.in);
            scan.nextLine();
            svc.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public StreamObserver<ImageRequest> sendImage(StreamObserver<ImageReply> responseObserver) {
        System.out.println("A Receber Imagem");
        try {
            StreamObserverImage streamObserverImage = new StreamObserverImage(responseObserver);
            return streamObserverImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void getImage(ReciveImageRequest request, StreamObserver<ReciveImageReply> responseObserver) {
        String imageName = "blob-" + request.getImageId();

        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        Firestore firestore = firestoreOptions.getService();

        CollectionReference colRef = firestore.collection("Dados-Imagens");
        DocumentReference docRef = colRef.document(imageName);
        ApiFuture<DocumentSnapshot> futureRef = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = futureRef.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String staticImageName = document.getString("static-name");
        byte[] origImage = new byte[0];
        byte[] staticImage = new byte[0];
        origImage = getImageBytesFromStorage("16-europa-16",imageName);
        staticImage = getImageBytesFromStorage("16-asia-16",staticImageName);


        ReciveImageReply reply = ReciveImageReply.newBuilder()
                .setOriginalimage(ByteString.copyFrom(origImage))
                .setStaticImage(ByteString.copyFrom(staticImage))
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    public byte[] getImageBytesFromStorage(String bucket, String imageName) {
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build();

        Storage storage = storageOptions.getService();
        BlobId blobId = BlobId.of(bucket, imageName);
        Blob blob = storage.get(blobId);
        byte[] imageBytes = blob.getContent();
        return imageBytes;
    }

    @Override
    public void listImages(ListImagesRequest request,StreamObserver<ListImagesReply> responseObserver) {

        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        Firestore firestore = firestoreOptions.getService();

        CollectionReference colRef = firestore.collection("Dados-Imagens");

        // Retrieve all documents in the collection
        ApiFuture<QuerySnapshot> querySnapshotFuture = colRef.get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = querySnapshotFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        // Iterate over the documents and retrieve the description field
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String firstStr = document.getId();
            String secondStr = document.getString("description");
            ids.add(firstStr);
            descriptions.add(secondStr);
        }

        int size = ids.size();
        String joinedDescriptions = String.join(", ", descriptions);
        String joinedIds = String.join(", ", ids);

        ListImagesReply reply = ListImagesReply.newBuilder()
                .setSize(size)
                .setDescricao(joinedDescriptions)
                .setImageId(joinedIds)
                .build();

        // Close the Firestore channel
        try {
            firestore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void getBetterScores(SocresImageRequest request,StreamObserver<SocresImageReply> responseObserver) {
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        Firestore firestore = firestoreOptions.getService();

        CollectionReference colRef = firestore.collection("Dados-Imagens");

        // Retrieve all documents in the collection
        ApiFuture<QuerySnapshot> querySnapshotFuture = colRef.get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = querySnapshotFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        // Iterate over the documents and retrieve the description field
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String firstStr = document.getId();
            String secondStr = document.getString("description");
            String thirdStr = document.getString("score");
            if(Float.parseFloat(thirdStr) >= 0.6){
                ids.add(firstStr);
                descriptions.add(secondStr);
                scores.add(thirdStr);
            }
        }

        int size = ids.size();
        String joinedScores = String.join(", ", scores);
        String joinedDescriptions = String.join(", ", descriptions);
        String joinedIds = String.join(", ", ids);

        SocresImageReply reply = SocresImageReply.newBuilder()
                .setSize(size)
                .setScore(joinedScores)
                .setImageId(joinedIds)
                .setDescricao(joinedDescriptions)
                .build();

        // Close the Firestore channel
        try {
            firestore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}



