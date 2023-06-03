package com.example.adsponsor.exception;

import com.example.adcommon.exception.AdException;
import com.example.adcommon.exception.ErrorDetails;
import com.example.adcommon.exception.GlobalExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @ControllerAdvice: handle exception globally
// ResponseEntityExceptionHandler class:handles and generates appropriate responses for exceptions thrown by Spring MVC applications in Java.
@ControllerAdvice
public class AdSponsorGlobalExceptionHandler extends GlobalExceptionHandler{

}
