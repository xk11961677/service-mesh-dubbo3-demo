## Attribute 和 Baggage 区别?

OpenTelemetry Attribute和Baggage都是OpenTelemetry规范中的概念，用于在分布式系统中传递信息和上下文。

Attribute是键值对，用于描述事件或请求的属性。它们可以帮助理解和分析系统的行为，并提供有关系统性能和行为的有用信息。
例如，Attribute可以包括请求的URL、响应时间、错误代码等。Attribute通常与Span一起使用，将它们附加到Span上，以便在分布式系统中跟踪请求的路径和执行情况。

Baggage是一种特殊的Attribute，它可以在分布式系统中传递上下文信息。Baggage是一个键值对集合，可以在不同的Span之间传递，
以便在整个分布式系统中跟踪请求和执行路径。Baggage通常用于传递与请求相关的信息，例如用户ID、会话ID等。Baggage的一个重要特点是它可以跨越多个服务和系统，
并且可以在不同的Span之间传递。

因此，Attribute和Baggage的区别在于它们的用途和传递的信息类型。Attribute通常用于描述请求或事件的属性，而Baggage用于传递上下文信息，在整个分布式系统中跟踪请求和执行路径。

## OpenTelemetry SDK OTEL_PROPAGATORS 环境变量是什么？(看有传递b3)

在OpenTelemetry-Java SDK中，OTEL_PROPAGATORS是一个环境变量，用于指定要使用的传播格式。它支持以下几种传播格式：

b3：使用Zipkin B3传播格式。
jaeger：使用Jaeger传播格式。
tracecontext：使用W3C TraceContext传播格式。
baggage：使用W3C Baggage传播格式。
xray：使用AWS X-Ray传播格式。
这些传播格式的具体含义如下：

Zipkin B3传播格式：该格式由Zipkin项目定义，用于在分布式系统中跟踪请求的路径和执行情况。它包括三个Header：X-B3-TraceId、X-B3-SpanId和X-B3-Sampled。
Jaeger传播格式：该格式由Jaeger项目定义，用于在分布式系统中跟踪请求的路径和执行情况。它包括两个Header：uber-trace-id和uberctx-。
W3C TraceContext传播格式：该格式由W3C定义，用于在分布式系统中跟踪请求的路径和执行情况。它包括四个Header：traceparent、tracestate、trace-id和span-id。
W3C Baggage传播格式：该格式由W3C定义，用于在分布式系统中传递上下文信息。它包括一个Header：traceparent。
AWS X-Ray传播格式：该格式由AWS X-Ray项目定义，用于在AWS云上跟踪请求的路径和执行情况。它包括两个Header：X-Amzn-Trace-Id和X-Amzn-Sampled。
因此，通过设置OTEL_PROPAGATORS环境变量，可以指定要使用的传播格式，以便在分布式系统中跟踪请求的路径和执行情况，或者传递上下文信息。