package service;

import io.grpc.stub.StreamObserver;
import service.ImageReply;
import java.util.ArrayList;
import java.util.List;

public class ClientImageStreamObserver implements StreamObserver<ImageReply> {

    private boolean isCompleted = false;
    private boolean success = false;

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean onSuccess() {
        return success;
    }

    List<String> replyList = new ArrayList<>();

    public List<String> getReplyList() {
        return replyList;
    }

    @Override
    public void onNext(ImageReply value) {
        System.out.println("Reply (" + value.getImageId() + ")");
        replyList.add(value.getImageId());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
        isCompleted = true;
        success = false;
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream Completed");
        isCompleted = true;
        success = true;
    }
}
