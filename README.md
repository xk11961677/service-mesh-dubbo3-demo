# dubbo3-demo

## 介绍
dubbo3 + triple

## 软件架构



## 使用说明

1. 启动前添加JVM参数 
```
--add-opens java.base/sun.net.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED
```
2. 启用opentelemetry
```
-javaagent:scripts/opentelemetry-javaagent.jar
-Dotel.resource.attributes=service.name=dubbo-consumer或者dubbo-provider

-Dotel.traces.exporter=jaeger
-Dotel.exporter.jaeger.endpoint=http://172.18.30.246:31730
-Dotel.metrics.exporter=none
-Dotel.propagators=b3

-- 或者
-Dotel.traces.exporter=otlp
-Dotel.exporter.otlp.endpoint=http://172.18.30.246:32242
-Dotel.metrics.exporter=none
-Dotel.propagators=b3




-- demo 
-javaagent:scripts/opentelemetry-javaagent.jar
-Dotel.resource.attributes=service.name=dubbo-demo-provider
-Dotel.traces.exporter=otlp
-Dotel.metrics.exporter=otlp
-Dotel.propagators=b3
-Dotel.exporter.otlp.endpoint=http://172.18.30.246:32242

-javaagent:scripts/opentelemetry-javaagent.jar
-Dotel.resource.attributes=service.name=dubbo-demo-consumer
-Dotel.traces.exporter=otlp
-Dotel.metrics.exporter=otlp
-Dotel.propagators=b3
-Dotel.exporter.otlp.endpoint=http://172.18.30.246:32242
```
[参数配置参考](https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md)
[官方文档](https://opentelemetry.io/docs/instrumentation/java/automatic/agent-config/)
[阿里云文档](https://help.aliyun.com/document_detail/413964.html)
[GITHUB](https://github.com/open-telemetry)
[DOCS-CN](https://github.com/open-telemetry/docs-cn)
[JAVA-DOCS](https://github.com/open-telemetry/opentelemetry-java-docs)

a) grafana http://172.18.30.246:30611/

b) prometheus http://172.18.30.246:30004/

c) kiali http://172.18.30.246:30003/

d) jaeger http://172.18.30.246:30415/jaeger


## 待完成
1. dubbo3中kiali流量监控有问题
2. docker 镜像后与opentelemetry结合使用

## 示例图片
![istio-dubbo](docs/images/istio-dubbo.png)

![tracing](docs/images/tracing.png)

![prometheus](docs/images/prometheus.png)

![grafana](docs/images/grafana.png)