package com.stannis.protobufbench.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter


@Configuration
class ProtobufConfig {
    @Bean
    fun protobufHttpMessageConverter(): ProtobufHttpMessageConverter {
        return ProtobufHttpMessageConverter()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProtobufConfig::class.java)
    }
}

