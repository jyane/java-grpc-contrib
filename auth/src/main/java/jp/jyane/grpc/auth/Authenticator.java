package jp.jyane.grpc.auth;

interface Authenticator {
  boolean authorize(String auth);
}
