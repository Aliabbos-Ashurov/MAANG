package com.abbos.maang;

import java.lang.annotation.*;

/**
 * example of anything
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-04-28
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Example {

    /**
     * A message describing the example
     */
    String message() default "";
}