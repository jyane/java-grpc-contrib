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

import static com.google.common.truth.Truth.assertThat;

import com.google.rpc.DebugInfo;
import io.grpc.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MoreStatusTest {
  @Test
  public void testfromStatus() throws Exception {
    com.google.rpc.Status status = MoreStatus.fromStatus(Status.INTERNAL);
    assertThat(status.getCode()).isSameAs(Status.INTERNAL.getCode().value());
    assertThat(status.getMessage()).isEqualTo("");
  }

  @Test
  public void testfromStatusWithMessage() throws Exception {
    com.google.rpc.Status status =
        MoreStatus.fromStatus(Status.INVALID_ARGUMENT.withDescription("foo"));
    assertThat(status.getCode()).isSameAs(Status.INVALID_ARGUMENT.getCode().value());
    assertThat(status.getMessage()).isEqualTo("foo");
  }

  @Test
  public void testfromStatusWithDetail() throws Exception {
    DebugInfo debugInfo = DebugInfo.newBuilder()
          .setDetail("bar")
          .build();
    com.google.rpc.Status status =
        MoreStatus.fromStatusWithDetails(Status.INTERNAL.withDescription("foo"), debugInfo);
    assertThat(status.getCode()).isSameAs(Status.INTERNAL.getCode().value());
    assertThat(status.getMessage()).isEqualTo("foo");
    assertThat(status.getDetails(0).unpack(DebugInfo.class).getDetail())
        .isEqualTo("bar");
  }

  @Test
  public void testfromStatusWithDetails() throws Exception {
    DebugInfo bar = DebugInfo.newBuilder()
            .setDetail("bar")
            .build();
    DebugInfo baz = DebugInfo.newBuilder()
            .setDetail("baz")
            .build();
    com.google.rpc.Status status =
        MoreStatus.fromStatusWithDetails(Status.INTERNAL.withDescription("foo"), bar, baz);
    assertThat(status.getCode()).isSameAs(Status.INTERNAL.getCode().value());
    assertThat(status.getMessage()).isEqualTo("foo");
    assertThat(status.getDetails(0).unpack(DebugInfo.class).getDetail())
        .isEqualTo("bar");
    assertThat(status.getDetails(1).unpack(DebugInfo.class).getDetail())
        .isEqualTo("baz");
  }
}
