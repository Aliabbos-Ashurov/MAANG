package com.abbos.maang.core.concurrency;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates the use of {@link CountDownLatch} in Java concurrency.
 * <p>
 * This program creates a fixed number of worker threads that perform a task and
 * decrement a shared {@link CountDownLatch}. The main thread waits until all workers
 * finish before proceeding.
 *
 * <p>
 * A {@link CountDownLatch} is used to block a thread until a set number of operations (performed by other threads) complete.
 * Unlike CyclicBarrier, it cannot be reused once the count reaches zero.
 * </p>
 *
 * @author Aliabbos Ashurov
 * @since 27/March/2025 13:34
 */
public class CountDownLatchExample {

    /**
     * Number of worker threads that must complete before the main thread proceeds.
     */
    private static final int NUM_OF_WORKERS = 4;

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(NUM_OF_WORKERS);

        for (int i = 0; i < NUM_OF_WORKERS; i++) {
            new Thread(new Worker(latch)).start();
        }

        try {
            // Main thread waits until all workers finish
            latch.await();
            System.out.println("All workers finished. Main thread proceeds.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Worker class representing an independent task that decrements the CountDownLatch.
     */
    private static final class Worker implements Runnable {

        private final CountDownLatch latch;

        public Worker(CountDownLatch countDownLatch) {
            this.latch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                // Simulate work being done by the worker thread
                System.out.println(Thread.currentThread().getName() + " is working...");
                Thread.sleep(new Random().nextInt(400, 1500)); // Random delay between 400ms and 1500ms
                System.out.println(Thread.currentThread().getName() + " has finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Decrement the CountDownLatch to signal task completion
                latch.countDown();
                System.out.printf("%s threads are on latch\n", latch.getCount());
            }
        }
    }
}
