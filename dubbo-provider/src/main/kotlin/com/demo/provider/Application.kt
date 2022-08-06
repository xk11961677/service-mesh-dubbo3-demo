package com.demo.provider

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.OpenTelemetry
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


/**
 */
@SpringBootApplication
@EnableDubbo
class Application {
    @Bean
    fun openTelemetry(): OpenTelemetry {
        return GlobalOpenTelemetry.get()
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
