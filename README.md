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


- **思考: 为什么统一响应?**
提高代码的一致性、可维护性、可测试性和安全性，同时提升用户体验。



