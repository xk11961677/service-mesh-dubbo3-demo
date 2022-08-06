package com.demo.api

interface UserApi {
    fun sayHello(name: String): String

    companion object{
        const val URL = "tri://dubbo-demo-provider:20880"
    }
}