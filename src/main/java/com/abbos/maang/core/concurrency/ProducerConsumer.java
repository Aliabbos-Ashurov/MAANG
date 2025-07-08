package com.abbos.maang.core.concurrency;

/**
 * Demonstrates producer-consumer patterns using different buffer implementations.
 *
 * @author Aliabbos Ashurov
 * @date March 28, 2025
 */
public class ProducerConsumer {

    /**
     * Entry point to run producer-consumer examples with semaphore and shared buffers.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        withSemaphoreBuffer();
        withSharedBuffer();
    }

    /**
     * Executes a producer-consumer scenario using a {@link SemaphoreBuffer} with integer data.
     * Launches separate threads for producing and consuming items.
     */
    private static void withSemaphoreBuffer() {
        SemaphoreBuffer<Integer> semaphoreBuffer = new SemaphoreBuffer<>();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreBuffer.produce(i);
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
            }
        }, "::Producer::").start();

        // Consumer Thread
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreBuffer.consume();
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }
            }
        }, "::Consumer::").start();
    }

    /**
     * Executes a producer-consumer scenario using a {@link SharedBuffer} with string data.
     * Launches separate threads for producing and consuming items.
     */
    private static void withSharedBuffer() {
        SharedBuffer<String> sharedBuffer = new SharedBuffer<>();

        // Producer Thread
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    sharedBuffer.produce(i + " banana");
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
            }
        }, "::Producer::").start();

        // Consumer Thread
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    sharedBuffer.consume();
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }
            }
        }, "::Consumer::").start();
    }
}