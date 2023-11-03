import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.google.protobuf") version "0.9.4"
}

group = "org.stannis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-kotlin
//    implementation("com.google.protobuf:protobuf-kotlin:3.25.0-RC2")
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-gradle-plugin
    runtimeOnly("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    // https://mvnrepository.com/artifact/io.grpc/grpc-kotlin-stub
    runtimeOnly("io.grpc:grpc-kotlin-stub:1.4.0")
    // https://mvnrepository.com/artifact/io.grpc/grpc-protobuf
    implementation("io.grpc:grpc-protobuf:1.59.0")
    // https://mvnrepository.com/artifact/io.grpc/grpc-stub
    implementation("io.grpc:grpc-stub:1.59.0")
    // https://mvnrepository.com/artifact/org.apache.tomcat/annotations-api
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.59.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
    // default proto plugin generate stub in build folder
    // change the stub generate folder
    // generatedFilesBaseDir = "$projectDir/src/generated"
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
        java {
            srcDir("build/generated/source/proto/main/grpc")
            srcDir("build/generated/source/proto/main/java")
        }
    }
}

tasks.withType<Copy> {
    filesMatching("**/*.proto") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
