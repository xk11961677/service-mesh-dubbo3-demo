package com.demo.provider.service

import com.demo.api.UserApi
import org.apache.dubbo.config.annotation.DubboService
import org.apache.dubbo.rpc.RpcContext
import org.slf4j.LoggerFactory

@DubboService
class UserApiImpl : UserApi {

    companion object {
        private val logger = LoggerFactory.getLogger(UserApiImpl::class.java)
    }

    override fun sayHello(name: String): String {
        logger.info("1生产端sayHello : {}$name")
        return "hello $name ， ServerContext="+ RpcContext.getServerContext().localAddress +" CurrentServiceContext="+ RpcContext.getCurrentServiceContext().localAddress
    }
}