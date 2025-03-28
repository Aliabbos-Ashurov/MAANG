package com.abbos.maang.core.concurrency;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrates asynchronous processing using CompletableFuture with a custom thread pool.
 *
 * @author Aliabbos Ashurov
 * @date March 28, 2025
 */
public class CompletableFutureExample {
    private static final Logger LOG = Logger.getLogger(CompletableFutureExample.class.getName());
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(THREAD_POOL_SIZE,
            r -> new Thread(r, "AsyncWorker-" + System.nanoTime()));

    static {
        LOG.setLevel(Level.ALL);
    }

    /**
     * Executes a pipeline of asynchronous tasks using CompletableFuture and logs the final result.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            CompletableFuture
                    .supplyAsync(() -> processStep("FIRST", 1), EXECUTOR)
                    .exceptionally(throwable -> {
                        LOG.log(Level.SEVERE, "Pipeline failed", throwable);
                        return "DEFAULT";
                    })
                    .thenApplyAsync(s -> processStep(s + " SECOND", 1), EXECUTOR)
                    .thenAccept(s -> System.out.printf("Final Result: %s%n", s + " THIRD"))
                    .join();
        } finally {
            shutdownExecutor();
        }
    }

    /**
     * Simulates a processing step with a delay, logging progress and handling interruptions.
     *
     * @param input the input string to process
     * @param delaySeconds the delay duration in seconds
     * @return the processed input string
     * @throws RuntimeException if interrupted during processing
     */
    private static String processStep(String input, int delaySeconds) {
        try {
            LOG.info("Processing: " + input);
            TimeUnit.SECONDS.sleep(delaySeconds);
            return input;
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "Interrupted during processing: " + input, ex);
            Thread.currentThread().interrupt(); // Restore interrupt status
            throw new RuntimeException("Interrupted in step: " + input, ex);
        }
    }

    /**
     * Shuts down the executor service gracefully, forcing termination if necessary.
     */
    private static void shutdownExecutor() {
        LOG.info("Shutting down executor");
        EXECUTOR.shutdown();
        try {
            if (!EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}