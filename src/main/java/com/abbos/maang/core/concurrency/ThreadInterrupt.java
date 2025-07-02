package com.abbos.maang.core.concurrency;

import java.util.logging.Logger;

/**
 * Demonstrates thread interruption handling in Java using two approaches:
 * <ul>
 *     <li>{@code interruptByMethod()} - Checks the interrupt flag and exits gracefully.</li>
 *     <li>{@code interruptByThrow()} - Throws a {@code RuntimeException} on interruption.</li>
 * </ul>
 *
 * Uses {@link Logger} for logging instead of {@code System.out}.
 *
 * @author Aliabbos Ashurov
 * @since 13/March/2025 12:45
 */
public class ThreadInterrupt {

    private static final Logger logger = Logger.getLogger(ThreadInterrupt.class.getName());
    
    public static void main(String[] args) {
        interruptByMethod();
        // interruptByThrow();
    }

    /**
     * Interrupts a thread by checking the {@code isInterrupted()} flag
     * and gracefully exiting the loop.
     */
    private static void interruptByMethod() {
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    logger.fine(Thread.currentThread().getName() + " interrupted. Exiting...");
                    return;
                }
                logger.fine(Thread.currentThread().getName() + " -> " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.fine(Thread.currentThread().getName() + " interrupted during sleep. Exiting...");
                    return;
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        logger.fine("Thread is working\n");

        thread.interrupt();
    }

    /**
     * Interrupts a thread by catching {@link InterruptedException}
     * and throwing a {@link RuntimeException}.
     */
    private static void interruptByThrow() {
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                logger.fine(Thread.currentThread().getName() + " -> " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Thread interrupted, throwing exception", e);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        logger.fine("Thread is working\n");

        thread.interrupt();
    }
}
