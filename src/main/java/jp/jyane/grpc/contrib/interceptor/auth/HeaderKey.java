package jp.jyane.grpc.contrib.interceptor.auth;

import io.grpc.Metadata;

public class HeaderKey {
  public static final Metadata.Key<String> AUTH_KEY
      = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
}
