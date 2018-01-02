package jp.jyane.grpc.auth;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.GreeterGrpc.GreeterBlockingStub;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.testing.GrpcServerRule;
import jp.jyane.grpc.auth.util.GreeterImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClientAuthInterceptorTest {
  @Rule
  public final GrpcServerRule grpcServerRule = new GrpcServerRule().directExecutor();

  private class ServerAuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {
      String auth = headers.get(
          Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER));
      if (auth != null && auth.equals("ok")) {
        return next.startCall(call, headers);
      }
      call.close(Status.UNAUTHENTICATED.withDescription("unauthenticated"), new Metadata());
      return new ServerCall.Listener<ReqT>() {};
    }
  }

  @Test
  public void testAuthorizationShouldPass() throws Exception {
    grpcServerRule.getServiceRegistry().addService(
        ServerInterceptors.intercept(new GreeterImpl(), new ServerAuthInterceptor()));
    GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(grpcServerRule.getChannel())
        .withInterceptors(new ClientAuthInterceptor("ok"));
    HelloReply reply = stub.sayHello(HelloRequest.newBuilder().setName("foo").build());
    assertThat(reply.getMessage()).isSameAs("foo");
  }

  @Test
  public void testAuthorizationShouldFail() throws Exception {
    grpcServerRule.getServiceRegistry().addService(
        ServerInterceptors.intercept(new GreeterImpl(), new ServerAuthInterceptor()));
    GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(grpcServerRule.getChannel())
        .withInterceptors(new ClientAuthInterceptor("not ok"));
    try {
      stub.sayHello(HelloRequest.newBuilder().setName("foo").build());
      fail("exception expected");
    } catch (StatusRuntimeException e) {
      Status status = Status.fromThrowable(e);
      assertThat(status.getCode()).isSameAs(Status.UNAUTHENTICATED.getCode());
    }
  }
}
