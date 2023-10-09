package service;


import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import io.grpc.stub.StreamObserver;


import service.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


class StreamObserverImage implements StreamObserver<ImageRequest> {

    private final StreamObserver<ImageReply> imageReplyStreamObserver;
    private String blobName;
    private static final String BUCKET_NAME =  "16-europa-16";
    private UUID imageId;
    private static final String PROJECT_NAME = "CN2223-T2-G10";
    private static final String PROJECT_ID = "cn2223-t2-g10";

    /*private final String APIKEY = "C:\\Users\\amvlf\\OneDrive\\Documents\\LEIM\\6sem\\CN\\cn2223-t2-g10-9a230a13c496.json";*/
    //private final String APIKEY = "cn2223-t2-g10-9a230a13c496.json";

    /*GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(APIKEY));*/
    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
    StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();
    Storage storage = storageOptions.getService();
    String projectID = storageOptions.getProjectId();

    //construtor da classe
    public StreamObserverImage(StreamObserver<ImageReply> imageReplyStreamObserver) throws IOException {
        this.imageReplyStreamObserver = imageReplyStreamObserver;
        imageId = UUID.randomUUID();
    }

    @Override
    public void onNext(ImageRequest value) {
        // Define the destination blob name (object name) in the bucket
        blobName = "blob-" + imageId.toString();
        // Create the blob in the bucket using the byte stream
        BlobId blobId = BlobId.of(BUCKET_NAME, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(value.getContentType())
                .build();

        storage.create(blobInfo, value.getData().toByteArray());

        System.out.println("File uploaded successfully.");
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error in StreamObserverImageRequest");
    }

    @Override
    public void onCompleted() {
            ImageReply imageReply = ImageReply.newBuilder().setImageId(imageId.toString()).build();
            imageReplyStreamObserver.onNext(imageReply);
            imageReplyStreamObserver.onCompleted();
            try {
                publishBlobName(blobName);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void publishBlobName(String blobName) throws IOException {
        Publisher publisher = Publisher.newBuilder(TopicName.of(PROJECT_ID, "projecto"))
                .setCredentialsProvider(() -> this.credentials)
                .build();

        // Create the message with the blob name
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(blobName))
                .build();

        // Publish the message to the Pub/Sub topic
        ApiFuture<String> messageId = publisher.publish(pubsubMessage);
        try {
            System.out.println("Published message: " + messageId.get());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error publishing message: " + e.getMessage());
        }
    }
}
