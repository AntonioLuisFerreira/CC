package service;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: ImagesService.proto")
public final class FileServiceGrpc {

  private FileServiceGrpc() {}

  public static final String SERVICE_NAME = "service.FileService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<service.ImageRequest,
      service.ImageReply> METHOD_SEND_IMAGE =
      io.grpc.MethodDescriptor.<service.ImageRequest, service.ImageReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "service.FileService", "sendImage"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.ImageRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.ImageReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<service.SocresImageRequest,
      service.SocresImageReply> METHOD_GET_BETTER_SCORES =
      io.grpc.MethodDescriptor.<service.SocresImageRequest, service.SocresImageReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "service.FileService", "getBetterScores"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.SocresImageRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.SocresImageReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<service.ReciveImageRequest,
      service.ReciveImageReply> METHOD_GET_IMAGE =
      io.grpc.MethodDescriptor.<service.ReciveImageRequest, service.ReciveImageReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "service.FileService", "getImage"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.ReciveImageRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.ReciveImageReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<service.ListImagesRequest,
      service.ListImagesReply> METHOD_LIST_IMAGES =
      io.grpc.MethodDescriptor.<service.ListImagesRequest, service.ListImagesReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "service.FileService", "listImages"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.ListImagesRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              service.ListImagesReply.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FileServiceStub newStub(io.grpc.Channel channel) {
    return new FileServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FileServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new FileServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FileServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new FileServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class FileServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *envia a imagem para lá
     * </pre>
     */
    public io.grpc.stub.StreamObserver<service.ImageRequest> sendImage(
        io.grpc.stub.StreamObserver<service.ImageReply> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_SEND_IMAGE, responseObserver);
    }

    /**
     * <pre>
     *recebe todos os scores acima de 0.6
     * </pre>
     */
    public void getBetterScores(service.SocresImageRequest request,
        io.grpc.stub.StreamObserver<service.SocresImageReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_BETTER_SCORES, responseObserver);
    }

    /**
     * <pre>
     *recebe as duas imagens a original e a static
     * </pre>
     */
    public void getImage(service.ReciveImageRequest request,
        io.grpc.stub.StreamObserver<service.ReciveImageReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_IMAGE, responseObserver);
    }

    /**
     */
    public void listImages(service.ListImagesRequest request,
        io.grpc.stub.StreamObserver<service.ListImagesReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_IMAGES, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SEND_IMAGE,
            asyncClientStreamingCall(
              new MethodHandlers<
                service.ImageRequest,
                service.ImageReply>(
                  this, METHODID_SEND_IMAGE)))
          .addMethod(
            METHOD_GET_BETTER_SCORES,
            asyncUnaryCall(
              new MethodHandlers<
                service.SocresImageRequest,
                service.SocresImageReply>(
                  this, METHODID_GET_BETTER_SCORES)))
          .addMethod(
            METHOD_GET_IMAGE,
            asyncUnaryCall(
              new MethodHandlers<
                service.ReciveImageRequest,
                service.ReciveImageReply>(
                  this, METHODID_GET_IMAGE)))
          .addMethod(
            METHOD_LIST_IMAGES,
            asyncUnaryCall(
              new MethodHandlers<
                service.ListImagesRequest,
                service.ListImagesReply>(
                  this, METHODID_LIST_IMAGES)))
          .build();
    }
  }

  /**
   */
  public static final class FileServiceStub extends io.grpc.stub.AbstractStub<FileServiceStub> {
    private FileServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FileServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FileServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *envia a imagem para lá
     * </pre>
     */
    public io.grpc.stub.StreamObserver<service.ImageRequest> sendImage(
        io.grpc.stub.StreamObserver<service.ImageReply> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(METHOD_SEND_IMAGE, getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *recebe todos os scores acima de 0.6
     * </pre>
     */
    public void getBetterScores(service.SocresImageRequest request,
        io.grpc.stub.StreamObserver<service.SocresImageReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_BETTER_SCORES, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *recebe as duas imagens a original e a static
     * </pre>
     */
    public void getImage(service.ReciveImageRequest request,
        io.grpc.stub.StreamObserver<service.ReciveImageReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_IMAGE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listImages(service.ListImagesRequest request,
        io.grpc.stub.StreamObserver<service.ListImagesReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_IMAGES, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class FileServiceBlockingStub extends io.grpc.stub.AbstractStub<FileServiceBlockingStub> {
    private FileServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FileServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FileServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *recebe todos os scores acima de 0.6
     * </pre>
     */
    public service.SocresImageReply getBetterScores(service.SocresImageRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_BETTER_SCORES, getCallOptions(), request);
    }

    /**
     * <pre>
     *recebe as duas imagens a original e a static
     * </pre>
     */
    public service.ReciveImageReply getImage(service.ReciveImageRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_IMAGE, getCallOptions(), request);
    }

    /**
     */
    public service.ListImagesReply listImages(service.ListImagesRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_IMAGES, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class FileServiceFutureStub extends io.grpc.stub.AbstractStub<FileServiceFutureStub> {
    private FileServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FileServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FileServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *recebe todos os scores acima de 0.6
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.SocresImageReply> getBetterScores(
        service.SocresImageRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_BETTER_SCORES, getCallOptions()), request);
    }

    /**
     * <pre>
     *recebe as duas imagens a original e a static
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.ReciveImageReply> getImage(
        service.ReciveImageRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_IMAGE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.ListImagesReply> listImages(
        service.ListImagesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_IMAGES, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_BETTER_SCORES = 0;
  private static final int METHODID_GET_IMAGE = 1;
  private static final int METHODID_LIST_IMAGES = 2;
  private static final int METHODID_SEND_IMAGE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FileServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FileServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_BETTER_SCORES:
          serviceImpl.getBetterScores((service.SocresImageRequest) request,
              (io.grpc.stub.StreamObserver<service.SocresImageReply>) responseObserver);
          break;
        case METHODID_GET_IMAGE:
          serviceImpl.getImage((service.ReciveImageRequest) request,
              (io.grpc.stub.StreamObserver<service.ReciveImageReply>) responseObserver);
          break;
        case METHODID_LIST_IMAGES:
          serviceImpl.listImages((service.ListImagesRequest) request,
              (io.grpc.stub.StreamObserver<service.ListImagesReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_IMAGE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendImage(
              (io.grpc.stub.StreamObserver<service.ImageReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class FileServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return service.ImagesService.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FileServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FileServiceDescriptorSupplier())
              .addMethod(METHOD_SEND_IMAGE)
              .addMethod(METHOD_GET_BETTER_SCORES)
              .addMethod(METHOD_GET_IMAGE)
              .addMethod(METHOD_LIST_IMAGES)
              .build();
        }
      }
    }
    return result;
  }
}
