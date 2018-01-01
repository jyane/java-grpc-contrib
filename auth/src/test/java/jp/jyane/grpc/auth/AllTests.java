package jp.jyane.grpc.auth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  ServerAuthInterceptorTest.class,
  ClientAuthInterceptorTest.class
})
public class AllTests {
}
