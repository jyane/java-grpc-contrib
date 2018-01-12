package jp.jyane.grpc.contrib.interceptor.auth;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerInterceptors;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.GreeterGrpc.GreeterBlockingStub;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.testing.GrpcServerRule;
import jp.jyane.grpc.contrib.util.GreeterImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ServerAuthInterceptorTest {
  @Rule
  public final GrpcServerRule grpcServerRule = new GrpcServerRule().directExecutor();

  private class ClientAuthInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
        CallOptions callOptions, Channel next) {
      return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
        @Override
        public void start(Listener<RespT> responseListener, Metadata headers) {
          headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "ok");
          super.start(new SimpleForwardingClientCallListener<RespT>(responseListener) {
            @Override
            public void onHeaders(Metadata headers) {
              super.onHeaders(headers);
            }
          }, headers);
        }
      };
    }
  }

  @Test
  public void testAuthorizationShouldPass() throws Exception {
    grpcServerRule.getServiceRegistry().addService(
        ServerInterceptors.intercept(new GreeterImpl(), new ServerAuthInterceptor(
            new Authenticator() {
              @Override
              public boolean authorize(String auth) {
                return auth.equals("ok");
              }
            })));
    GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(grpcServerRule.getChannel())
        .withInterceptors(new ClientAuthInterceptor());
    HelloReply reply =
        stub.sayHello(HelloRequest.newBuilder().setName("foo").build());
    assertThat(reply.getMessage()).isSameAs("foo");
  }

  @Test
  public void testAuthorizationShouldFail() throws Exception {
    grpcServerRule.getServiceRegistry().addService(
        ServerInterceptors.intercept(new GreeterImpl(), new ServerAuthInterceptor(
            new Authenticator() {
              @Override
              public boolean authorize(String auth) {
                return auth.equals("not ok");
              }
            })));
    GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(grpcServerRule.getChannel())
        .withInterceptors(new ClientAuthInterceptor());
    try {
      stub.sayHello(HelloRequest.newBuilder().setName("foo").build());
      fail("exception expected");
    } catch (StatusRuntimeException e) {
      Status status = Status.fromThrowable(e);
      assertThat(status.getCode()).isSameAs(Status.UNAUTHENTICATED.getCode());
    }
  }

  @Test
  public void testInterceptorShouldWorkWithLambda() throws Exception {
    grpcServerRule.getServiceRegistry().addService(
        ServerInterceptors.intercept(new GreeterImpl(), new ServerAuthInterceptor(auth -> true)));
    GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(grpcServerRule.getChannel())
        .withInterceptors(new ClientAuthInterceptor());
    HelloReply reply =
        stub.sayHello(HelloRequest.newBuilder().setName("foo").build());
    assertThat(reply.getMessage()).isSameAs("foo");
  }
}
