package com.demo.consumer.controller

import com.demo.consumer.service.OrderService
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import io.opentelemetry.extension.annotations.SpanAttribute
import io.opentelemetry.extension.annotations.WithSpan
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource


/**
 */
@RestController
class UserController {

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)

    }

    @Resource
    private lateinit var orderService: OrderService

    @GetMapping("/hello")
    fun hello():String {
        return orderService.sayHello()
    }

    @GetMapping("/tracing")
    fun asyncHello():String {
        logger.info("测试链路追踪")
        val span = Span.current()
        // 添加自定义tag数据
        span.setAttribute("user.id", "123456")
        Thread(Context.current().wrap(Runnable {
          withNewSpan("线程内创建新span")
        })).start()
        withNewSpan("创建新span")
        //GlobalOpenTelemetry.get().getTracer(Application::class.java.name)
        // 类似于日志, 比如在 trace 中嵌入自定义属性日志
        span.addEvent("asyncHello.start", atttributes("demo-consumer"))
        span.addEvent("asyncHello.end", atttributes("demo-consumer"))

        //todo slw Baggage ，添加在 metrics、log、traces 中的注解信息，键值对需要唯一，无法更改


        return buildURL(span.spanContext.traceId)
    }



    @WithSpan
    private fun withNewSpan(@SpanAttribute("user.type") userType: String) {
        logger.info("测试链路追踪$userType")
        orderService.sayHello()
    }

    private fun buildURL(traceId:String):String {
        return "http://172.18.30.246:30415/jaeger/trace/${traceId}"
    }

    private fun atttributes(id: String): Attributes {
        return Attributes.of(AttributeKey.stringKey("app.id"), id)
    }
}