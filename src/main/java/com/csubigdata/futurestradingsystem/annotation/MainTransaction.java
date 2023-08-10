package com.csubigdata.futurestradingsystem.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface MainTransaction {
    int value();//子线程数量
}
