package com.abbos.maang.core.concurrency;

import com.abbos.maang.annotation.Tag;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-14
 */
class ThreadBehaviours {

    @Test
    void threadStaticMethods() {
        System.out.println(Thread.activeCount());
        System.out.println(Thread.currentThread());
        System.out.println(Thread.getAllStackTraces());
        Thread.currentThread().interrupt();
        System.out.println(Thread.interrupted());
    }

    @Test
    void threadCurrentMethods() {
        // current thread
        System.out.println(Thread.currentThread().getThreadGroup());
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getState());
        System.out.println(Thread.currentThread().isAlive());
    }

    @SneakyThrows
    @Test
    void daemonThread() {
        System.out.println(Thread.currentThread().isDaemon()); // REMEMBER: if it is virtual it always returns true!

        Thread.ofPlatform()
                .daemon(true)
                .name("Thread-OfPlatform-daemon")
                .start(() -> {
                    System.out.println(Thread.currentThread().isDaemon()); // prints true because we set daemon
                });
        Thread.ofPlatform()
                .daemon(false)
                .name("Thread-OfPlatform-not-daemon")
                .start(() -> {
                    System.out.println(Thread.currentThread().isDaemon()); // prints false
                });
        Thread.ofVirtual() // we don't have .daemon( bool ) method
                .name("Thread-OfVirtual-not-daemon")
                .start(() -> {
                    System.out.println(Thread.currentThread().isDaemon()); // prints true as i said before V Threads returns true always
                });
        Thread.startVirtualThread(() -> {
            System.out.println(Thread.currentThread().isDaemon()); // true
        });
    }

    @Test
    void isThreadVirtual() {
        System.out.println(Thread.currentThread().isVirtual());
    }


    @Test
    void test_virtual_threads() {
        long start = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(
                    i ->
                            executor.submit(() -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                if (i % 1000 == 0) System.out.println("Done " + i);
                            })
            );
        }

        long end = System.currentTimeMillis();
        System.out.println("Elapsed: " + (end - start) + " ms");
    }


    @Test
    void test_platform_threads() throws InterruptedException {
        @Cleanup ExecutorService executor = Executors.newFixedThreadPool(200);

        long start = System.currentTimeMillis();

        IntStream.range(0, 10_000).forEach(
                i ->
                        executor.submit(() -> {
                            try {
                                Thread.sleep(1000);
                                if (i % 1000 == 0) System.out.println("Done " + i);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        })
        );

        executor.shutdown();
        if (executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }


        long end = System.currentTimeMillis();
        System.out.println("Elapsed: " + (end - start) + " ms");
    }

    @Test
    void threadExceptionHandlerWithLambda() {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Hi");
        });
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " :: " + e.getMessage());
        });
        thread.start();
    }


    @SuppressWarnings("ClassCanBeRecord")
    private static class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private final Runnable action;

        public CustomUncaughtExceptionHandler(Runnable action) {
            this.action = action;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            this.action.run();
        }

        public static CustomUncaughtExceptionHandler ofDefault() {
            return of(() -> {
                System.out.println("It is time to sleep!");
            });
        }

        public static CustomUncaughtExceptionHandler of(Runnable action) {
            return new CustomUncaughtExceptionHandler(action);
        }
    }

    @Test
    void threadExceptionHandlerWithImpl() {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("anyway");
        });
        thread.setUncaughtExceptionHandler(CustomUncaughtExceptionHandler.ofDefault());
        thread.start();
    }

    @Test
    @Tag("New way of building threads in JDK 21")
    void threadBuilder() {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Hi");
        });
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " :: " + e.getMessage());
        });
        thread.start();
    }
}
