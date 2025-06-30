package com.abbos.maang.annotation;

import java.lang.annotation.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-04-28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface Tag {
    
    String[] value();
}
