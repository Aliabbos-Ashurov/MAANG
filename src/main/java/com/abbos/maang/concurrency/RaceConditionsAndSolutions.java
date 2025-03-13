package com.abbos.maang.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Aliabbos Ashurov
 * @since 13/March/2025  15:04
 **/
public class RaceConditionsAndSolutions {

    private static final Logger logger = Logger.getLogger(RaceConditionsAndSolutions.class.getName());
    private static final int ITERATIONS = 10000;
    private static final int THREAD_POOL_SIZE = 2;

    static {
        logger.setLevel(Level.ALL);
    }

    public static void main(String[] args) {
        try {
            runOnUnsnychronizedMode();
            runOnSynchronizedMode();
            runOnAtomicMode();
            runOnLockedMode();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Execution failed", e);
        }
    }


    private static void runOnUnsnychronizedMode() {
        logger.info("Running unsynchronized counter...");
        BasicCounter counter = new BasicCounter();
        run(counter);
    }

    private static void runOnSynchronizedMode() {
        logger.info("Running synchronized counter...");
        SynchronizedCounter counter = new SynchronizedCounter();
        run(counter);
    }

    private static void runOnAtomicMode() {
        logger.info("Running atomic counter...");
        AtomicCounter counter = new AtomicCounter();
        run(counter);
    }

    private static void runOnLockedMode() {
        logger.info("Running atomic counter...");
        LockedCounter counter = new LockedCounter();
        run(counter);
    }

    private static void run(Counter<? extends Number> counter) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            logger.fine(() -> threadName + " started execution");
            for (int i = 0; i < ITERATIONS; i++) {
                counter.increment();
            }
            logger.fine(() -> threadName + " completed execution");
        };

        // Submit tasks and wait for completion
        CompletableFuture<?>[] futures = new CompletableFuture[THREAD_POOL_SIZE];
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            futures[i] = CompletableFuture.runAsync(task, executor);
        }

        // Wait for all tasks to complete
        CompletableFuture.allOf(futures).join();
        executor.shutdown();

        logger.info(() -> "Final count: " + counter.getCounter() +
                " (Expected: " + (ITERATIONS * THREAD_POOL_SIZE) + ")");

        executor.close();

    }

    private interface Counter<T extends Number> extends java.io.Serializable {
        T getCounter();

        void increment();
    }

    private static final class BasicCounter implements Counter<Integer> {
        private int counter = 0;

        @Override
        public Integer getCounter() {
            return counter;
        }

        @Override
        public void increment() {
            counter++;
        }
    }

    private static final class SynchronizedCounter implements Counter<Integer> {
        private int counter = 0;

        @Override
        public Integer getCounter() {
            return counter;
        }

        @Override
        public synchronized void increment() {
            counter++;
        }
    }

    private static final class AtomicCounter implements Counter<Integer> {
        private final AtomicInteger atomic = new AtomicInteger(0);

        @Override
        public Integer getCounter() {
            return atomic.get();
        }

        @Override
        public void increment() {
            atomic.incrementAndGet();
        }
    }

    private static final class LockedCounter implements Counter<Integer> {
        private final Lock lock = new ReentrantLock();
        private int counter = 0;

        @Override
        public Integer getCounter() {
            return counter;
        }

        @Override
        public void increment() {
            lock.lock();
            try {
                counter++;
            } finally {
                lock.unlock();
            }
        }
    }
}
