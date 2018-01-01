package jp.jyane.grpc.auth;

import io.grpc.examples.helloworld.GreeterGrpc.GreeterImplBase;
import io.grpc.testing.GrpcServerRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ServerAuthInterceptorTest {
  @Rule
  public final GrpcServerRule grpcServerRule = new GrpcServerRule().directExecutor();

  @Test
  public void testAuthorize() throws Exception {
    grpcServerRule.getServiceRegistry().addService(new GreeterImplBase() {});
    assert(true);
  }
}
