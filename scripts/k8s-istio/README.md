## 以下文件为networking  istio
ingress-gateway.yaml
egress-gateway.yaml

## 以下文件为k8s namespace inject to istio
ns.yaml

## 以下文件为k8s platform inject to istio
consumer.yaml
provider.yaml
service.yaml
### 定义路由规则与目标规则
vm-service.yaml

## 以下文件为ServiceEntry ，用于网格外服务交由网格管理，也可提供外部访问
service-entry.yaml

## 以下文件为policy envoy rate filter

## 以下文件为envoy主动通过grpc检测provider是否存活
envoy-filter.yaml
[envoy健康检查配置手册](https://www.envoyproxy.io/docs/envoy/latest/api-v3/config/core/v3/health_check.proto#envoy-v3-api-msg-config-core-v3-healthcheck)

## test-ingress-gateway.sh脚本 测试从网关请求到dubbo-provider权重

## [Kustomize管理yaml清单](https://kubernetes.io/zh-cn/docs/tasks/manage-kubernetes-objects/kustomization/)

[阿里云社区文章](https://developer.aliyun.com/article/941534)

### 目录说明
    base目录基础模板
    dev目录下存放开发环境定制清单
    stag目录下存放预发环境定制清单
    prod目录下存放生产环境定制清单

### 使用命令
```
#使用命令将所有文件串联并导出prod-all.yaml ，(在scripts/k8s-istio/overlays/prod目录下操作)
kubectl kustomize ./ > prod-all.yaml

#发布使用prod目录 （集群版本要高于1.14）
kubectl apply -k ./

#删除prod目录资源
kubectl delete -k ./
```

### 约束

* 修改vr与dr顺序

  在添加版本子集的时候，需要先修改 DR ，等 DR 生效后再修改 VS。

  在删除版本子集的时候， 需要先修改 VS，等 VS 生效后再修改 DR。
* yaml文件属性需要使用FQDN：(Fully Qualified Domain Name)
* grpc 服务配置自定义的重试策略，避免istio默认的重试5次。(todo)
* ingress-gateway中router需要有默认的 subset: default