package com.abbos.maang.core.concurrency;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrates handling uncaught exceptions in threads using a custom exception handler.
 * <p>
 * The program sets a global {@code UncaughtExceptionHandler} and starts a thread that
 * deliberately throws a {@code RuntimeException} when interrupted.
 * </p>
 *
 * @author Aliabbos Ashurov
 * @since 13/March/2025 13:06
 */
public class ThreadExceptionHandler {

    private static final Logger logger = Logger.getLogger(ThreadExceptionHandler.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.ALL);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                logger.info(String.format("Thread data :: %s | cycle :: %d", Thread.currentThread().getName(), i));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        logger.fine("Thread is working");
        thread.interrupt();
    }

    /**
     * Custom exception handler that logs uncaught exceptions for threads.
     */
    private static final class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
        /**
         * Handles uncaught exceptions by logging the thread name and exception details.
         *
         * @param t the thread that encountered the exception
         * @param e the uncaught exception
         */
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.log(Level.INFO, "Thread name :: {0}, Exception :: {1}", new Object[]{t.getName(), e.getMessage()});
        }
    }
}
