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

package jp.jyane.grpc.contrib.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.google.rpc.Status.Builder;
import io.grpc.Status;
import javax.annotation.Nonnull;

/**
 * Utility class for {@link com.google.rpc.Status} and {@link io.grpc.Status}
 */
public class MoreStatus {
  private MoreStatus() {}

  private static Builder createBuilderFromStatus(Status status) {
    checkNotNull(status, "status");
    return com.google.rpc.Status.newBuilder()
        .setCode(status.getCode().value())
        .setMessage(Strings.nullToEmpty(status.getDescription()));
  }

  /**
   * creates {@link com.google.rpc.Status} from {@link io.grpc.Status}
   * @param status {@link io.grpc.Status}
   * @return {@link com.google.rpc.Status}
   */
  public static com.google.rpc.Status fromStatus(Status status) {
    checkNotNull(status, "status");
    return createBuilderFromStatus(status).build();
  }

  /**
   * creates {@link com.google.rpc.Status} from {@link io.grpc.Status} and
   * {@link com.google.protobuf.Message}
   * @param status {@link io.grpc.Status}
   * @param details {@link com.google.protobuf.Message}
   * @return {@link com.google.rpc.Status}
   */
  public static com.google.rpc.Status fromStatusWithDetails(Status status, Message... details) {
    checkNotNull(status, "status");
    Builder statusProtoBuilder = createBuilderFromStatus(status);
    for (Message message : details) {
      statusProtoBuilder.addDetails(Any.pack(message));
    }
    return statusProtoBuilder.build();
  }
}
