package com.example.adcommon.advice;

import com.example.adcommon.exception.AdException;
import com.example.adcommon.responseUtils.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

// 定义全局的异常处理和响应处理逻辑
@RestControllerAdvice
public class GlobalExceptionAdvice {

    // 处理特定类型的异常AdException
    @ExceptionHandler(value = AdException.class)
    public CommonResponse<String> handlerAdException(HttpServletResponse req, AdException adException) {

        CommonResponse<String> response1 = new CommonResponse<>(500, "business error");
        response1.setData(adException.getMessage());
        return response1;
    }


}
