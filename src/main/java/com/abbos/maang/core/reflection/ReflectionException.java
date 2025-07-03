package com.abbos.maang.core.reflection;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-08
 */
public class ReflectionException extends RuntimeException {
    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
