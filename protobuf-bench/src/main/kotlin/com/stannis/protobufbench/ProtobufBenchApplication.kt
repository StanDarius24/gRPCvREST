package com.stannis.protobufbench

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProtobufBenchApplication

fun main(args: Array<String>) {
    runApplication<ProtobufBenchApplication>(*args)
}
