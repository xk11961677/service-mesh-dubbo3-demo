package com.demo.consumer.service

import com.demo.api.UserApi
import org.apache.dubbo.config.annotation.DubboReference
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderService {

    companion object {
        private val logger = LoggerFactory.getLogger(OrderService::class.java)
    }
    // todo slw 测试连接数问题
    @DubboReference(url=UserApi.URL,check=false, proxy = "javassist", connections = 10)
    private lateinit var userApi: UserApi

    fun sayHello(): String {
        logger.info("1消费端sayHello")
        return userApi.sayHello("aaaa")
    }
}