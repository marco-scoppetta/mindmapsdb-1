load("@graknlabs_build_tools//checkstyle:rules.bzl", "checkstyle_test")

java_library(
    name = "graql-test-util",
    srcs = ["GraqlTestUtil.java"],
    visibility = ["//test-integration:__subpackages__"],
    deps = [
        "//concept:concept",
        "//server:server",

        "@graknlabs_graql//java:graql",

        "//dependencies/maven/artifacts/commons-collections:commons-collections",
        "//dependencies/maven/artifacts/junit:junit"
    ]
)

checkstyle_test(
    name = "checkstyle",
    targets = [
        ":graql-test-util"
    ],
)
