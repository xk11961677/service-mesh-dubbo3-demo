package com.demo.consumer.manager

import com.demo.consumer.service.OrderService
import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.metrics.LongCounter
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement
import io.opentelemetry.extension.annotations.WithSpan
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

/**
 * 增加定时任务，测试流量治理
 */
@Component
class Schedule(private val orderService: OrderService,private val openTelemetry: OpenTelemetry) {

    companion object {
        private val logger = LoggerFactory.getLogger(Schedule::class.java)
        private val threadFactory = ThreadFactoryBuilder().setDaemon(true).setNameFormat("scheduled-task-%d").build()
        private val executor = Executors.newSingleThreadScheduledExecutor(threadFactory)
        private var directoryCounter: LongCounter? = null
    }

    @PostConstruct
    fun init() {
        executor.scheduleAtFixedRate({
            try{
                exec()
            }catch (t: Throwable) {
                logger.error("定时任务执行异常: ",t)
            }
        },3000,2000, TimeUnit.MILLISECONDS)
    }

    @WithSpan
    private fun exec() {
        orderService.sayHello()

        execMetrics()
    }

    //metrics test
    private fun execMetrics() {
        if(directoryCounter == null) initMetrics()

        directoryCounter!!.add(1,Attributes.of(AttributeKey.stringKey("app.id"), "demo-consumer"))
    }

    private fun initMetrics() {
        val sampleMeter = openTelemetry.getMeter("dubbo-demo-consumer")
        directoryCounter = sampleMeter
            .counterBuilder("directories_search_count")
            .setDescription("Counts directories accessed while searching for files.")
            .setUnit("unit")
            .build()

        sampleMeter.gaugeBuilder("jvm.memory.total")
            .setDescription("Reports JVM memory usage.")
            .setUnit("byte")
            .buildWithCallback { result: ObservableDoubleMeasurement ->
                result.record(
                    Runtime.getRuntime().totalMemory().toDouble(), Attributes.empty()
                )
            }
    }
}