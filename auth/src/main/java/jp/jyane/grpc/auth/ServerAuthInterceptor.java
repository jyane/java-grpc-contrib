package jp.jyane.grpc.auth;

import com.google.common.base.Preconditions;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class ServerAuthInterceptor implements ServerInterceptor {
  private final Authenticator authenticator;

  public ServerAuthInterceptor(Authenticator authenticator) {
    Preconditions.checkNotNull(authenticator, "authenticator");
    this.authenticator = authenticator;
  }

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {
    String auth = headers.get(HeaderKey.AUTH_KEY);
    if (authenticator.authorize(auth)) {
      return next.startCall(call, headers);
    }
    call.close(Status.UNAUTHENTICATED.withDescription("unauthenticated"), new Metadata());
    return new ServerCall.Listener<ReqT>() {};
  }
}
