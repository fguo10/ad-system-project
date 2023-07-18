package com.example.adsearch.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/* 实现了单例模式的索引缓存工具类。通过ApplicationContext获取Spring容器中的Bean实例，
并且通过dataTableMap缓存已获取的实例，以提高性能。在使用时，可以通过调用DataTable.of(clazz)来获取指定类对应的实例。*/

@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {
    private static ApplicationContext applicationContext;
    public static final Map<Class, Object> dataTableMap;

    static {
        dataTableMap = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("all")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }


    // 根据beanName获取Bean
    @SuppressWarnings("all")
    private static <T> T bean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    // 根据Class类型获取Bean
    @SuppressWarnings("all")
    private static <T> T bean(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    // 如果bean已经加载过,直接返回dataTableMap的instance。
    // 如果bean从未加载过，则加载并且缓存到dataTableMap，并返回缓存的instance。
    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz) {
        T instance = (T) dataTableMap.get(clazz);
        if (instance != null) {
            return instance;
        }
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

}
