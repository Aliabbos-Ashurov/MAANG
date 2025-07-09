package com.abbos.maang.ocp;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Aliabbos Ashurov
 * @since 2025-05-29
 */
public class Streams {

    @Test
    public void predicateTest() {
        Predicate<String> empty = String::isEmpty;
        Predicate<String> notEmpty = empty.negate();

        var result = Stream.generate(() -> "")
                .filter(notEmpty)
                .collect(Collectors.groupingBy(k -> k))
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.partitioningBy(notEmpty));

        System.out.println(result);
    }
}
