package com.abbos.maang.core.stream;

import java.util.Objects;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-03
 */
@FunctionalInterface
public interface MapperFunction<T, R> {

    R map(T source);

    default <V> MapperFunction<T, V> andThen(MapperFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.map(map(t));
    }
}
