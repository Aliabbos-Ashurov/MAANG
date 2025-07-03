package com.abbos.maang.core.reflection;

import com.google.errorprone.annotations.DoNotMock;
import com.google.errorprone.annotations.Immutable;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-08
 */
@DoNotMock
@Immutable
public final class Messages {
    public static class Reflection {
        public static final String TARGET_CLASS_NULL = "Target class must not be null";
        public static final String METHOD_NAME_NULL = "Method name must not be null";
        public static final String FIELD_NAME_NULL = "Field name must not be null";
        public static final String FILTER_NULL = "Filter must not be null";
    }

    private Messages() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
