package com.abbos.maang.core.concurrency;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A thread-safe buffer implementation for producer-consumer scenarios, utilizing a queue with a fixed capacity.
 * This class supports generic types that extend {@link Serializable} and provides synchronized methods for
 * adding and removing elements.
 */
class SharedBuffer<T extends Serializable> {

    private final Queue<T> queue = new LinkedList<>();
    private static final int CAPACITY = 5;

    /**
     * Adds an element to the buffer in a thread-safe manner. If the buffer reaches its maximum capacity,
     * the calling thread waits until space becomes available. After adding the element, all waiting threads
     * (typically consumers) are notified to resume their operations.
     *
     * @param data the element to be added to the buffer
     * @throws IllegalStateException if the operation is interrupted or encounters an unexpected error
     */
    public synchronized void produce(T data) {
        try {
            while (queue.size() == CAPACITY) {
                wait();
            }
            queue.add(data);
            System.out.printf("%s added to queue\n", data);
            notifyAll(); // Notify waiting consumers
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to produce data due to an interruption or error", e);
        }
    }

    /**
     * Removes and processes an element from the buffer in a thread-safe manner. If the buffer is empty,
     * the calling thread waits until an element is available. After removing the element, all waiting threads
     * (typically producers) are notified that space is available in the buffer.
     *
     * @throws IllegalStateException if the operation is interrupted or encounters an unexpected error
     */
    public synchronized void consume() {
        try {
            while (queue.isEmpty()) {
                wait();
            }
            T poll = queue.poll();
            System.out.printf("%s consumed from queue\n", poll);
            notifyAll(); // Notify waiting producers
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to consume data due to an interruption or error", e);
        }
    }
}