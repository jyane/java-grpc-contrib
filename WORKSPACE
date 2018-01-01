workspace(name = "java_grpc_interceptors")

http_archive(
    name = "grpc_java",
    strip_prefix = "grpc-java-1.8.0",
    urls = ["https://github.com/grpc/grpc-java/archive/v1.8.0.zip"],
)

load("@grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()

maven_jar(
    name = "junit",
    artifact = "junit:junit:4.12",
)

maven_jar(
    name = "grpc_testing",
    artifact = "io.grpc:grpc-testing:1.8.0",
)

maven_jar(
    name = "com_google_truth_truth",
    artifact = "com.google.truth:truth:jar:0.37",
)

