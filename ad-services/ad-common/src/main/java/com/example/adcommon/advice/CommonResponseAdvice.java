package com.example.adcommon.advice;

import com.example.adcommon.annotation.IgnoreResponseAdvice;
import com.example.adcommon.responseUtils.CommonResponse;
import jakarta.annotation.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

// handle exceptions and customize the response body for all requests in a RESTful API.
@RestControllerAdvice
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

    // 是否支持拦截
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }


    // 写入响应之前做的操作
    // @Nullable: indicate that a parameter, return value, or field of a method or class can be null
    // @SuppressWarnings("all"): suppress compiler warnings for all types of warnings i
    @Override
    @Nullable
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        CommonResponse<Object> response1 = new CommonResponse<>(200, "");
        if(body == null){
            return response1;
        } else if (body instanceof CommonResponse) {
            response1 = (CommonResponse<Object>) body;
        }else{
            response1.setData(body);
        }

        return response1;
    }
}
