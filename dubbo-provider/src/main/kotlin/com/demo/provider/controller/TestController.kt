package com.demo.provider.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


/**
 */
@RestController
class TestController {

    companion object {
        private val logger = LoggerFactory.getLogger(TestController::class.java)
    }

    @GetMapping("/test")
    fun test():String {
        logger.info("调用provider test接口")
        return "dubbo-demo-provider"
    }
}