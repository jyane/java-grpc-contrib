package jp.jyane.grpc.auth.util;

import io.grpc.examples.helloworld.GreeterGrpc.GreeterImplBase;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;

public class GreeterImpl extends GreeterImplBase {
  @Override
  public void sayHello(HelloRequest request,
      io.grpc.stub.StreamObserver<HelloReply> responseObserver) {
    HelloReply reply = HelloReply.newBuilder().setMessage(request.getName()).build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }
}
