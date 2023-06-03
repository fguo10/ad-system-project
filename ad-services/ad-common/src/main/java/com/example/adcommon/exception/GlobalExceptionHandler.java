package com.example.adcommon.exception;

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
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // ExceptionHandler annotation used to handle the specific exceptions and sending the custom responses to client.
    @ExceptionHandler(AdException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(AdException exception, WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), webRequest.getDescription(false), "AdException");

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }


    // ExceptionHandler annotation used to handle the specific exceptions and sending the custom responses to client.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), webRequest.getDescription(false), "INTERNAL SERVER ERROR");

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        /*
        When form data binding fails, the BindingResult object stores all the error messages,
        including error codes and descriptions. The getBindingResult() method is used to retrieve
        the result object bound with the request parameters, while the getAllErrors() method is used
        to retrieve a list of all error messages stored in the result object as ObjectError objects.
        Storing them in a list allows for further processing or returning to the client.
         */

        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();

        // errorList.forEach() is used in Java 8 to iterate through a collection of elements。
        errorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
