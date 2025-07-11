package com.abbos.maang.core.concurrency;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * Demonstrates the use of {@link CyclicBarrier} in Java.
 * <p>
 * This program creates a fixed number of worker threads that perform a task and
 * then wait at a synchronization barrier before continuing. When all workers reach the barrier,
 * a predefined action executes before they proceed.
 *
 * <p>
 * A {@link CyclicBarrier} is used in Java to synchronize multiple threads at a common point.
 * It allows a set number of threads to wait for each other before proceeding further.
 * Once all threads reach the barrier, it resets and can be reused.
 * <p/>
 *
 * @author Aliabbos Ashurov
 * @since 27/March/2025 13:57
 */
public class CyclicBarrierExample {

    /**
     * Number of worker threads that must reach the barrier before proceeding.
     */
    private static final int NUM_OF_WORKERS = 4;

    public static void main(String[] args) {

        // Create a CyclicBarrier for NUM_OF_WORKERS threads
        // When all threads reach the barrier, the provided Runnable executes
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_OF_WORKERS, () -> {
            System.out.println("All workers are here!");
        });

        // Start NUM_OF_WORKERS threads
        for (int i = 0; i < NUM_OF_WORKERS; i++) {
            new Thread(new Worker(cyclicBarrier)).start();
        }
    }

    /**
     * Worker class representing an independent task that waits at the CyclicBarrier.
     */
    @SuppressWarnings("Convert2Record")
    private static final class Worker implements Runnable {

        private final CyclicBarrier cyclicBarrier;

        public Worker(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                // Simulate work being done by the worker thread
                System.out.printf("%s started working...%n", Thread.currentThread().getName());
                Thread.sleep(100 + new Random().nextInt(200)); // Random delay between 100ms and 300ms
                System.out.printf("%s has finished its job%n", Thread.currentThread().getName());

                // Wait at the barrier until all threads arrive
                cyclicBarrier.await();
            } catch (Exception e) {
                throw new RuntimeException("Worker encountered an error", e);
            }
        }
    }
}
