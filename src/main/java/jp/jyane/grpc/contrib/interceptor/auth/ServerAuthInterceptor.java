/*
 * Copyright 2018 jyane.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.jyane.grpc.contrib.interceptor.auth;

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
