# 微服务架构设计实现广告系统

项目基于Spring Cloud 微服务架构设计实现广告系统，其中包括两个核心模块：广告投放系统(ad-sponsor)和广告检索系统(ad-search)。

## 功能需求(Functional Requirements)

**广告投放系统**

负责管理广告投放的相关功能，包括用户账户，推广计划，推广单元，创意之间的管理，并设定推广单元的限制表，以控制广告投放的目标受众。
限制表管理基于三个维度进行限制：关键词、地域和兴趣。

- 关键词限制：设定关键词列表，只有在匹配关键词的用户才会看到广告。
- 地域限制：指定广告投放的地理位置范围，只有在指定地区的用户才会看到广告。
- 兴趣限制：根据用户的兴趣爱好设定广告投放的目标受众。

![ad-sponsor-concepts.png](images%2Fad-sponsor-concepts.png)

**广告检索系统**
广告检索系统是**媒体方通过添加限定条件**向广告检索系统发起请求来获取符合条件的广告创意信息。广告检索的业务数据流如下图:

![ad-search-workflow.png](images%2Fad-search-workflow.png)

## 非功能性需求(Non-functional requirements)

- 响应时间：系统应具有快速的响应时间，以确保用户在合理的时间内获得广告内容。
- 可扩展性：系统应能够根据需求灵活扩展，以适应未来的增长和负载增加。
- 可用性：系统应保持高可靠性和稳定性，减少系统故障和不可用时间。
- 可维护性: 系统的代码结构清晰且记录日志和异常信息，以便快速排查和解决问题。

# 高级设计(High-Level Design)

![high-level-design.png](images%2Fhigh-level-design.png)

## API 设计(API Design)

使用RESTful API, 遵循REST 架构规范的应用编程接口（API）, 具体的API设计如下:

### 广告投放系统API设计

**用户账户API设计**

| API                                  | Details            | Success Status | 
|--------------------------------------|--------------------|----------------|
| POST /ad_sponsor/api/v1/ad_plan      | 创建广告账户,安全角度考虑,密码加密 | 201            |
| GET /ad_sponsor/api/v1/ad_user/login | 广告账户登录             | 200            |

请求的参数如下:

| Field    | Description  | Type   |
|----------|--------------|--------|
| username | 账户名          | String |
| token    | token信息,也是密码 | String |

**推广计划API设计**

| API                                    | Details                                               | Success Status |
|----------------------------------------|-------------------------------------------------------|----------------|
| POST /ad_sponsor/api/v1/ad_user        | 创建推广计划,默认status=valid, 自动创建和更新create_time和update_time | 201            |
| GET /ad_sponsor/api/v1/ad_plan         | 搜索推广计划,可以根据userId和adPlanIds搜索                         | 200            |
| PUT /ad_sponsor/api/v1/ad_plan/{id}    | 更新推广计划,可以根据userId和adPlanIds搜索                         | 200            |
| DELETE /ad_sponsor/api/v1/ad_plan/{id} | 删除推广计划                                                | 200            |

**推广单元API设计**

| API                                            | Details                                               | Success Status |
|------------------------------------------------|-------------------------------------------------------|----------------|
| POST /ad_sponsor/api/v1/ad_unit                | 创建推广单元,默认status=valid, 自动创建和更新create_time和update_time | 201            |
| POST /ad_sponsor/api/v1/ad_unit/{id}/keywords  | 关联推广单元和关键词限制,每次可关联多个关键字                               | 200            |
| POST /ad_sponsor/api/v1/ad_unit/{id}/interests | 关联推广单元和兴趣限制,每次可关联多个兴趣标签                               | 200            |
| POST /ad_sponsor/api/v1/ad_unit/{id}/areas     | 关联推广单元和地域限制 ,每次可关联多个地域                                | 200            |
| POST /ad_sponsor/api/v1/ad_unit/creatives      | 批量关联推广单元和创意                                           | 200            |

**创意API设计**

| API                                 | Details                                             | Success Status |
|-------------------------------------|-----------------------------------------------------|----------------|
| POST /ad_sponsor/api/v1/ad_creative | 创建创意,默认status=valid, 自动创建和更新create_time和update_time | 201            |

### 广告搜索系统API设计

| API                           | Details                   | Success Status |
|-------------------------------|---------------------------|----------------|
| POST /ad_search/api/v1/search | 根据媒体方+限制条件返回可以展示在广告位的创意信息 | 200            |

请求的字段信息:

| Field       | Description | Type   |
|-------------|-------------|--------|
| mediaId     | 媒体方的请求标识    | String |
| RequestInfo | 请求基本信息      | json   |
| FeatureInfo | 匹配信息        | json   |

响应的自段信息:

| Field      | Description         | Type |
|------------|---------------------|------|
| adSlot2Ads | 根据请求的广告位返回符合条件的创意列表 | json |

## 数据模型(Data Model)

**数据模型概述**

- 用户账户和推广计划是一对多关系。
- 推广计划和推广单元是一对多关系。

![data-model-1.png](images%2Fdata-model-1.png)

- 推广单元和3个维度的限制(关键词、地域和兴趣)是一对多关系。

![data-model-2.png](images%2Fdata-model-2.png)

- 推广单元和创意是多对多关系。

![data-model-3.png](images%2Fdata-model-3.png)

# Deep Dive

## 广告数据索引设计-Optimize the Ad-search using JVM Index

广告数据索引设计旨在提高广告检索的效率，基于需求，构建正向索引和倒置索引。[代码详情](ad-services%2Fad-search%2Fsrc%2Fmain%2Fjava%2Fcom%2Fexample%2Fadsearch%2Findex)

- 正向索引通过主键生成与对象的映射关系，比如:
    - `Map<Long, AdPlanObject>`生成id和推广计划对象的映射关系
    - `Map<Long, AdUnitObject>`生成id和推广单元对象的映射关系
    - `Map<Long, CreativeObject>`生成id和创意对象的映射关系
    - `Map<Long, Set<String>`生成id和3个不同维度限制对象的映射关系
    - `Map<String, CreativeUnitObject>` String代表creativeId-unitId的拼接信息，生成和推广单元创意表对象的映射关系。

- 反向索引用于存储在全文搜索下某个单词在一个文档或者一组文档中存储位置的映射关系。
    - `Map<String, Set<Long>> keywordUnitMap`: 根据提供的关键词返回关联的推广对象。
    - `Map<String, Set<Long>> districtUnitMap`： 根据提供的地域信息返回关联的推广对象。
    - `Map<String, Set<Long>> itUnitMap`： 根据提供的兴趣信息返回关联的推广对象。

![index-design.png](images%2Findex-design.png)

## 广告检索系统 – 加载全量索引和增量索引

将全部广告数据加载到系统的索引中，以便在进行广告检索时能够访问和搜索所有的广告信息。工作流如下:

- 导出数据: 广告投放模块(ad-sponsor)的所有广告数据导出到文件中。
- 全量索引: 对所有的广告数据构建基于JVM的索引，并在每次重启ad-search模块时自动加载。
- 增量索引：todo, 计划使用mysql Binlog构建增量数据。
- 构造单例模式的索引缓存工具类

## 广告匹配策略设计

- 广告位类型预过滤：根据广告位的positionType，从广告库中选择初始的AdUnit列表，该列表包含了可能与广告位匹配的广告创意单元（AdUnit）。
- 特征过滤与关联关系处理：利用广告位的特征信息，结合广告创意的关联关系，在初始AdUnit列表中进行进一步过滤，以缩小匹配范围。
- 广告位条件过滤创意对象: 确保广告创意的尺寸与广告位的尺寸要匹配，保证广告显示效果。

## 响应与异常统一实现

统一实现响应和异常处理，确保系统在遇到异常情况时能够给出适当的响应和处理。

# 项目部署

- 容器化技术: 基于Dockerfile文件构建镜像，打包和部署微服务。
- CICD: 使用持续集成和持续交付（CI/CD）工具，例如Jenkins、GitLab CI。
- 服务注册与发现：Eureka。