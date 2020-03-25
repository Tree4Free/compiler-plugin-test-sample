# Sample kotlin compiler plugin

## Issue

Maven wont run without the `kotlin-compiler` dependency, `kotlin-compile-testing` wont run without the kotlin-compiler-embeddable dependency used.

The compiler dependencies are specified in `/compiler-plugin/pom.xml`