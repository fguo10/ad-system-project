
# 微服务架构设计实现广告系统

本项目旨在实现一个广告系统，其中包括两个核心模块：广告投放系统和广告检索系统。


## 功能需求(Functional Requirements)

**广告投放系统**

负责管理广告投放的相关功能，包括用户账户，推广计划，推广单元，创意之间的管理，以及基于推广单元三个维度(关键词,地域和兴趣)的限制表管理。

![ad-sponsor-concepts.png](images%2Fad-sponsor-concepts.png)

**广告检索系统**
媒体方根据限定的条件


## 非功能性需求(Non-functional requirements)


# High-Level Design

## API 设计(API Design)


## 数据模型(High-level Architecture)


## Data Model


# Deep Dive

## 广告数据索引设计-Optimize the Ad-search using JVM Index
广告数据索引设计旨在提高广告检索的效率，包括以下方面：

- 关键词索引：建立广告文本中关键词的索引，以支持基于关键词的检索。
- 受众属性索引：建立广告的目标受众属性的索引，以支持基于目标受众属性的检索。

## ORM服务接口实现

## 响应与异常统一实现
统一实现响应和异常处理，确保系统在遇到异常情况时能够给出适当的响应和处理。



# 项目部署





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
which forwards the call to the appropriate services on the back
end.[link](https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/microservices)

- port: 8080
- 思考: 为什么实现API Gateway不使用zuul而是Spring Cloud Gateway? 为什么使用AWS API Gateway?

Since Spring Cloud Netflix project, including Zuul, has been put into maintenance mode, the recommended approach is to
use Spring Cloud Gateway as the API gateway solution.
Spring Cloud Gateway is the successor to Zuul in the Spring Cloud ecosystem and provides similar functionalities with
improved performance and flexibility.

- 思考： 如何使用SpringCloud
  Gateway? [Spring Cloud Gateway的官方文档](https://cloud.spring.io/spring-cloud-gateway/reference/html/)

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

# 广告投放系统(Ad delivery system)
## 数据库表
- 用户账户(ad_user): username, token, user_status, create_time, update_time
- 推广计划(ad_plan): user_id, plan_name, plan_status, start_date, end_date,  create_time, update_time
- 推广单元(ad_unit): plan_id, unit_name, unit_status, position_type(广告位类型: 开屏,贴片 etc), budget, create_time, update_time
- 维度限制表:
  - 关键词限制(ad_unit_keyword): unit_id, keyword
  - 地域限制(ad_unit_district): unit_id, state, city
  - 兴趣限制(ad_unit_interest)：unit_id, interest_tag
- 创意(ad_creative): 

**数据库表关系**: 推广计划和推广单元是一对多关系, 推广单元和创意是多对多关系。





## 思考：广告投放系统的限制维度怎么扩展?
设计一个广告投放系统，其中推广单元有3个维度的限制，关键词、地域和兴趣，根据企业真实情况，限制的维度还有哪些呢？

- 广告主要指定受众群体：企业可能希望将广告仅展示给特定的人群，如特定年龄段、性别、职业等。通过这种方式，广告可以更加精准地针对目标受众，提高广告的效果。
- 设备和平台：企业可能希望将广告仅展示在特定的设备或平台上，例如仅在移动设备上展示广告、仅在特定的社交媒体平台上展示广告等。
- 时间和频次：企业可能希望限制广告的展示时间和频次，以控制广告投放的节奏和频率。例如，广告可能仅在特定的时间段内展示，或者限制每个用户每天只能看到一定次数的广告。
- 语言和地域设置：除了地域限制外，企业可能还希望限制广告的语言和地域设置。这可以确保广告仅展示给特定语言的用户或者仅在特定的地理区域展示。
- 受众行为：企业可以基于用户的行为和兴趣进行广告投放的限制。例如，可以仅将广告展示给在过去一段时间内表现出与企业产品或服务相关兴趣的用户。
- 预算和投放目标：广告投放系统可以根据企业的预算和投放目标进行限制。例如，企业可以设置每日广告费用上限或者要求广告在特定时间内达到一定的展示量或转化率。




