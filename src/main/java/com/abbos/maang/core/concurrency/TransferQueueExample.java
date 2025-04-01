package com.abbos.maang.core.concurrency;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * a small and well-structured example of {@link TransferQueue<>}
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-03-29
 */
public class TransferQueueExample {
    public static void main(String[] args) {
        TransferQueue<String> queue = new LinkedTransferQueue<>();

        Thread producerThread = new Thread(() -> {
            try {
                System.out.println("Producer starting...");

                // 1. Basic transfer - blocks until a consumer takes it
                System.out.println("Producer attempting to transfer Item1");
                queue.transfer("Item1");
                System.out.println("Producer completed transfer of Item1");

                Thread.sleep(1000); // Simulate some processing time

                // 2. Try transfer with timeout - returns false if times out
                System.out.println("Producer attempting timed transfer of Item2");
                boolean transferred = queue.tryTransfer("Item2", 2, TimeUnit.SECONDS);
                System.out.println("Timed transfer success: " + transferred);

                // 3. Check if there are waiting consumers
                System.out.println("Waiting consumers present: " + queue.hasWaitingConsumer());
                System.out.println("Number of waiting consumers: " + queue.getWaitingConsumerCount());

                // 4. Non-blocking tryTransfer - returns immediately
                System.out.println("Producer attempting non-blocking transfer of Item3");
                boolean success = queue.tryTransfer("Item3");
                System.out.println("Non-blocking transfer success: " + success);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }
        }, "::{PRODUCER}::");

        Thread consumerThread = new Thread(() -> {
            try {
                // Simulate delayed consumer start
                Thread.sleep(2000);
                System.out.println("Consumer starting...");

                // 1. Take an item - blocks until available
                String item1 = queue.take();
                System.out.println("Consumer received: " + item1);

                Thread.sleep(1000); // Simulate processing time

                // 2. Poll with timeout - returns null if times out
                String item2 = queue.poll(1, TimeUnit.SECONDS);
                System.out.println("Consumer polled: " + (item2 != null ? item2 : "null"));

                // 3. Regular poll - non-blocking
                String item3 = queue.poll();
                System.out.println("Consumer polled: " + (item3 != null ? item3 : "null"));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }
        }, "::{CONSUMER}::");

        producerThread.start();
        consumerThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
