# dubbo3-demo

## 介绍
dubbo3 + triple

## 软件架构



## 使用说明
0. 前提
```
配置环境变量或修改gradle.properties文件，添加私服与阿里云镜像账号密码
export NEXUS_USERNAME=私服用户名
export NEXUS_PASSWORD=私服密码
export JIB_USERNAME=阿里云镜像账号
export JIB_PASSWORD=阿里云镜像密码
```
1. 启动前添加JVM参数 
```
--add-opens java.base/sun.net.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED
```
2. 启用opentelemetry添加JVM参数
```
-javaagent:scripts/extra/opentelemetry-javaagent.jar
-Dotel.resource.attributes=service.name=dubbo-demo-consumer或者dubbo-demo-provider

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
-javaagent:scripts/extra/opentelemetry-javaagent.jar
-Dotel.resource.attributes=service.name=dubbo-demo-provider
-Dotel.traces.exporter=otlp
-Dotel.metrics.exporter=otlp
-Dotel.propagators=b3
-Dotel.exporter.otlp.endpoint=http://172.18.30.246:32242

-javaagent:scripts/extra/opentelemetry-javaagent.jar
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


```可观测地址
a) grafana http://172.18.30.246:30611/

b) prometheus http://172.18.30.246:30004/

c) kiali http://172.18.30.246:30003/

d) jaeger http://172.18.30.246:30415/jaeger

e) 消费端入口访问地址: http://172.18.30.246/dubbo-demo-consumer/hello | http://172.18.30.246/dubbo-demo-consumer/tracing
```

3. [google jib 镜像打包工具](https://github.com/GoogleContainerTools/jib)
     [如何添加参数到镜像](https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md#how-do-i-set-parameters-for-my-image-at-runtime)

4. [Kustomize管理yaml清单](https://kubernetes.io/zh-cn/docs/tasks/manage-kubernetes-objects/kustomization/)

```
#使用命令将所有文件串联并导出prod-all.yaml ，(在scripts/k8s-istio/overlays/prod目录下操作)
kubectl kustomize ./ > prod-all.yaml

#发布使用prod目录 （集群版本要高于1.14）
kubectl apply -k ./

#删除prod目录资源
kubectl delete -k ./
```

5. 使用k8s部署前，请在对应namespace添加secret资源
```
kubectl create secret docker-registry aliyun \
--docker-server=registry.cn-hangzhou.aliyuncs.com \
--docker-username=阿里云镜像仓库名称 \
--docker-password=阿里云镜像仓库密码 \
--docker-email=邮箱 \
-n [namespace]
```

6. [k8s切换namespace等功能](https://github.com/sbstp/kubie)

7. 测试从网关请求到dubbo-provider权重
```
# 执行scripts/k8s-istio/test-ingress-gateway.sh脚本
chmod +x test-ingress-gateway.sh
sh test-ingress-gateway.sh
```

## 待完成
1. 先使用springboot作为存活探针，后期可使用dubbo自定义存活与就绪探针(官方支持扩展)
2. k8s configmap springboot热更新
3. 现阶段使用kustomize管理需要自己手动创建namespace，另使用namePrefix，项目DubboReference的url也需要改(application.properties配置)
4. 需要使用kustomize替换镜像版本和环境变量等


## 示例图片
![istio-dubbo](docs/images/istio-dubbo.png)

![istio-vm-weight](docs/images/istio-vm-weight.png)

![istio-dubbo-opentelemetry](docs/images/istio-dubbo-opentelemetry.png)

![tracing](docs/images/tracing.png)

![tracing-istio](docs/images/tracing-istio.png)

![prometheus](docs/images/prometheus.png)

![grafana](docs/images/grafana.png)
