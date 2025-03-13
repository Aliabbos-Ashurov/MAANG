package com.abbos.maang.concurrency;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Aliabbos Ashurov
 * @since 13/March/2025  14:07
 **/
public class BasicRaceCondition {

    private static final Logger logger = Logger.getLogger(BasicRaceCondition.class.getName());
    private static final int ITERATIONS = 0b1111101000; // 1000

    static {
        logger.setLevel(Level.ALL);
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Runnable runnable = () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                counter.increment();
            }
        };

        Thread th1 = new Thread(runnable);
        Thread th2 = new Thread(runnable);

        th1.start();
        th2.start();

        Thread.sleep(2000);
        System.out.println(counter.getCount());
    }

    private static final class Counter implements java.io.Serializable {
        private int count;

        {
            count = 0;
        }

        public Counter of(int initial) {
            this.count = initial;
            return new Counter();
        }

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }
}
