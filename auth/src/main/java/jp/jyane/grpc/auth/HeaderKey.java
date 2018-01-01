package jp.jyane.grpc.auth;

import io.grpc.Metadata;

public class HeaderKey {
  public static final Metadata.Key<String> AUTH_KEY
      = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
}
