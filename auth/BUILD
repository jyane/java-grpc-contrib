load("@grpc_java//:java_grpc_library.bzl", "java_grpc_library")

java_library(
    name = "auth",
    srcs = glob(["src/main/java/**/*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "@grpc_java//core",
    ],
)

java_test(
    name = "auth-tests",
    srcs = glob(["src/test/java/**/*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "@grpc_java//core",
        "@grpc_java//core:util",
        "@grpc_java//core:inprocess",
        "@grpc_java//stub",
        "@grpc_testing//jar",
        "@junit//jar",
        "@com_google_truth_truth//jar",
        ":auth",
        ":helloworld_java_grpc",
        ":helloworld_java_proto",
    ],
    test_class = "jp.jyane.grpc.auth.AllTests",
)

proto_library(
    name = "helloworld_proto",
    srcs = ["src/test/proto/helloworld.proto"],
)

java_proto_library(
    name = "helloworld_java_proto",
    deps = [":helloworld_proto"],
)

java_grpc_library(
    name = "helloworld_java_grpc",
    srcs = [":helloworld_proto"],
    deps = [":helloworld_java_proto"],
)
