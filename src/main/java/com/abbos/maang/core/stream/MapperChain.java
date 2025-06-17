package com.abbos.maang.core.stream;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-03
 */
@FunctionalInterface
public interface MapperChain<S, T> {

    T apply(S source);

    default <V> MapperChain<S, V> andThen(MapperFunction<? super T, V> after) {
        return source -> after.map(apply(source));
    }
}
