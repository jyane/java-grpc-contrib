package jp.jyane.grpc.contrib.interceptor.auth;

interface Authenticator {
  boolean authorize(String auth);
}
