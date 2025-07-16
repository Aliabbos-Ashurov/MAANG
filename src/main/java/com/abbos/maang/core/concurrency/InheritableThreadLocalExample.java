package com.abbos.maang.core.concurrency;

import com.abbos.maang.annotation.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-14
 */
@Tag("concurrency")
class InheritableThreadLocalExample {

    @Test
    void run() throws InterruptedException {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();


        Thread thread1 = Thread.ofPlatform()
                .name("Thread 1")
                .unstarted(() -> {
                    threadLocal.set("Thread 1 - thread local");
                    inheritableThreadLocal.set("Thread 1 - inheritable thread local");

                    System.out.println("[Thread 1] ThreadLocal: " + threadLocal.get());
                    System.out.println("[Thread 1] InheritableThreadLocal: " + inheritableThreadLocal.get());

                    Thread thread2 = Thread.ofPlatform()
                            .name("Thread 2")
                            .unstarted(() -> {
                                System.out.println("[Thread 2] ThreadLocal: " + threadLocal.get());
                                System.out.println("[Thread 2] InheritableThreadLocal: " + inheritableThreadLocal.get());
                            });

                    thread2.start();
                    try {
                        thread2.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

        thread1.start();
        thread1.join();
    }
}
