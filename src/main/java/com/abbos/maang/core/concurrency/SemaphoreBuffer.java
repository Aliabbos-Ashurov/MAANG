package com.abbos.maang.core.concurrency;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * A thread-safe buffer using semaphores for producer-consumer synchronization with a fixed capacity.
 *
 * @author Aliabbos Ashurov
 * @date March 28, 2025
 */
public class SemaphoreBuffer<T extends Serializable> {

    private static final int CAPACITY = 5;
    private final Queue<T> queue = new LinkedList<>();
    private final Semaphore empty = new Semaphore(0);
    private final Semaphore full = new Semaphore(CAPACITY);
    private final Semaphore mutex = new Semaphore(1);

    /**
     * Adds an element to the buffer, waiting if no space is available. Notifies consumers when done.
     *
     * @param data the element to add
     */
    public void produce(T data) {
        try {
            empty.acquire(); // Wait for an empty slot
            mutex.acquire(); // Lock for exclusive access
            queue.add(data);
            System.out.println("Produced: " + data);
            mutex.release();
            full.release(); // Signal item available
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * Removes and returns an element from the buffer, waiting if empty. Notifies producers when space is freed.
     *
     * @return the consumed element, or null if interrupted
     */
    public T consume() {
        try {
            full.acquire(); // Wait for an item
            mutex.acquire(); // Lock for exclusive access
            T poll = queue.poll();
            System.out.println("Consumed: " + poll);
            mutex.release();
            empty.release(); // Signal space available
            return poll;
        } catch (InterruptedException ignore) {
        }
        return null;
    }
}