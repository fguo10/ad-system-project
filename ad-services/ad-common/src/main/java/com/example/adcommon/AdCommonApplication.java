package com.example.adcommon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication: 应用程序可以自动扫描并注册需要的组件，同时基于类路径和条件，自动配置应用程序。
// @SpringBootApplication = @EnableAutoConfiguration + @ComponentScan + @Configuration
//      - @EnableAutoConfiguration: 启用自动配置机制, 根据应用程序的依赖和配置信息，自动配置和加载各种组件、库和功能。
//      - @ComponentScan: Spring在指定的包路径下进行扫描，以找到带有这些注解的组件，并将它们纳入Spring上下文中。
//      - @Configuration: 表示一个类是配置类
@SpringBootApplication
public class AdCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdCommonApplication.class, args);
    }

}
