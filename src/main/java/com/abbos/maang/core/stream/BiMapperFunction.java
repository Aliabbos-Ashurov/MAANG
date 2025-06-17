package com.abbos.maang.core.stream;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-03
 */
@FunctionalInterface
public interface BiMapperFunction<S, C, T> {

    T map(S source, C context);
}
