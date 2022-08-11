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
        logger.info("生产端sayHello : {}$name")
        val isProviderSide = RpcContext.getContext().isProviderSide
        val clientIP = RpcContext.getContext().remoteHost
        val remoteApplication = RpcContext.getContext().remoteApplicationName
        val application = RpcContext.getContext().url.getParameter("application")
        val protocol = RpcContext.getContext().protocol
        return "hello, $name , response from provider-v1: ${RpcContext.getContext().localAddress} , client: $clientIP, " +
                "local: $application, remote: $remoteApplication , isProviderSide: $isProviderSide , protocol: $protocol"
    }
}