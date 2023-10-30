plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.google.protobuf") version "0.9.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-kotlin
    implementation("com.google.protobuf:protobuf-kotlin:3.25.0-RC2")
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-gradle-plugin
    runtimeOnly("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
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
