package com.abbos.maang.core.stream;

import java.util.function.Predicate;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-03
 */
public interface MapperDSL<S, T> {

    <V> MapperDSL<S, V> mapTo(MapperFunction<? super S, V> mapper);

    MapperDSL<S, T> withContext(BiMapperFunction<? super S, ?, T> mapper);

    MapperDSL<S, T> when(Predicate<S> condition, MapperFunction<? super S, T> mapper);

    T execute(S source);
}