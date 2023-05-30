package com.example.adcommon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


// 1). WebMvcConfigurer: Spring MVC 中进行自定义配置
//      eg: 对请求处理、视图解析、拦截器、资源处理等方面进行自定义设置。
// 2). @Configuration: 特殊类型的 Spring bean，定义和组织其他 bean 的创建和配置。用于标识一个类作为配置类
@Configuration
public class WebConfiguration implements WebMvcConfigurer {


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 清空转换器
        converters.clear();
        // 添加新的转换器，将java对象转换为json类型
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
