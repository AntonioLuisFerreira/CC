// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ImagesService.proto

package service;

public interface ImageRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:service.ImageRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bytes data = 1;</code>
   */
  com.google.protobuf.ByteString getData();

  /**
   * <code>string contentType = 2;</code>
   */
  java.lang.String getContentType();
  /**
   * <code>string contentType = 2;</code>
   */
  com.google.protobuf.ByteString
      getContentTypeBytes();

  /**
   * <code>int32 size = 3;</code>
   */
  int getSize();
}
