package com.example.adcommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// @interface used to define a new annotation type.
// @Target({ElementType.TYPE, ElementType.METHOD}): specific custom annotation can be applied to both classes and methods.
// @Retention(RetentionPolicy.RUNTIME): annotation will be available at runtime
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {


}
