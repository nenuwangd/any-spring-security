package com.spring4all.annotation;

import java.lang.annotation.*;
import java.util.List;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String[] authorities() default {};
    String[] roles() default {} ;
}
