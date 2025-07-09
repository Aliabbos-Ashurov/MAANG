package com.abbos.maang.core.stream;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A concurrent and unordered {@link Collector} implementation that collects stream elements
 * into a thread-safe {@link ConcurrentLinkedQueue} and then returns them as an immutable {@link List}.
 * <p>
 * This collector is designed for efficient parallel stream processing where order is not important.
 * </p>
 *
 * @param <T> the type of input elements to be collected
 * @author Aliabbos Ashurov
 */
public class ConcurrentStreamCollector<T> implements Collector<T, ConcurrentLinkedQueue<T>, List<T>> {

    /**
     * Provides a new {@link ConcurrentLinkedQueue} instance to accumulate elements.
     */
    @Override
    public Supplier<ConcurrentLinkedQueue<T>> supplier() {
        return ConcurrentLinkedQueue::new;
    }

    /**
     * Adds an element to the concurrent queue.
     */
    @Override
    public BiConsumer<ConcurrentLinkedQueue<T>, T> accumulator() {
        return ConcurrentLinkedQueue::add;
    }

    /**
     * Merges two {@link ConcurrentLinkedQueue} instances into one.
     */
    @Override
    public BinaryOperator<ConcurrentLinkedQueue<T>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    /**
     * Converts the accumulated {@link ConcurrentLinkedQueue} into an immutable {@link List}.
     */
    @Override
    public Function<ConcurrentLinkedQueue<T>, List<T>> finisher() {
        return List::copyOf;
    }

    /**
     * Specifies the characteristics of this collector.
     * <ul>
     *   <li>{@link Characteristics#CONCURRENT} - Allows concurrent accumulation.</li>
     *   <li>{@link Characteristics#UNORDERED} - The collected elements do not maintain a specific order.</li>
     * </ul>
     *
     * @return a set of characteristics for this collector
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED);
    }
}
