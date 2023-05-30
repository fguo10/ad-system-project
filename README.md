# Eureka server: service discovery
- port: 8001, 8002, 8003

- How to build jar file?
```bash
mvn clean package -Dmaven.test.skip=true -U
```

- How to run jar file with specific profile?
```bash
java -jar ad-eureka-1.0-SNAPSHOT.jar --spring.profiles.active=server1
```

- 思考: 为什么netflix-eureka-client在springboot 3.1之后不支持了?

  Netflix在2018年宣布停止对Eureka Server和相关组件的开发和维护, 将重心转移到更现代化的服务注册与发现方案上。
  其他替代方案: Spring Cloud Consul或Spring Cloud ZooKeeper来进行服务注册与发现。

# API Gateway
API gateway is the entry point for clients. Instead of calling services directly, clients call the API gateway,
which forwards the call to the appropriate services on the back end.[link](https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/microservices)


- port: 8080
- 思考: 为什么实现API Gateway不使用zuul而是Spring Cloud Gateway? 为什么使用AWS API Gateway?

Since Spring Cloud Netflix project, including Zuul, has been put into maintenance mode, the recommended approach is to use Spring Cloud Gateway as the API gateway solution.
Spring Cloud Gateway is the successor to Zuul in the Spring Cloud ecosystem and provides similar functionalities with improved performance and flexibility.


- 思考： 如何使用SpringCloud Gateway? [Spring Cloud Gateway的官方文档](https://cloud.spring.io/spring-cloud-gateway/reference/html/)


# Ad Common模块

## 功能
- 通用代码定义，配置定义
- 统一的响应处理
- 统一的异常处理


## 思考: 为什么响应对象需要统一格式?
提高代码的一致性、可维护性、可测试性和安全性，同时提升用户体验。

- 统一客户端处理：使客户端在处理不同微服务的响应时更加统一和方便。客户端可以基于一致的响应格式进行解析和处理，无需针对每个微服务的响应进行特定的处理逻辑。
- 减少沟通成本：不同团队开发的微服务可以通过约定统一的响应格式来交互，而无需详细讨论和协商每个接口的响应结构。提高开发效率，并降低团队之间的集成难度。
- 错误处理一致性：统一的响应格式可以确保错误处理在微服务之间保持一致。
- 服务间解耦：通过统一的响应格式，微服务之间可以更好地解耦。
- 可追踪性和监控：统一的响应格式可以包含关键的元数据，例如请求ID、时间戳等，以便于追踪和分析请求的流程和性能指标。


## 思考: 除了通用的AdException之外, 还会设计哪些自定义类呢？这样设计的理由是什么？
- 业务异常类（BusinessException）
- 验证异常类（ValidationException）
- 授权异常类（AuthorizationException）
- 文件上传/下载异常类（FileOperationException）
- 远程服务调用异常类（RemoteServiceException）


- add unit operation