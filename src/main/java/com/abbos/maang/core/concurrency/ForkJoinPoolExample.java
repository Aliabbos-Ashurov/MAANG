package com.abbos.maang.core.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * Demonstrates the use of ForkJoinPool for parallel processing in Java.
 * This example includes two tasks: finding the longest filename (RecursiveTask)
 * and capitalizing a list of filenames (RecursiveAction).
 *
 * @author Aliabbos Ashurov
 * @since 17/March/2025 13:07
 */
public class ForkJoinPoolExample {

    private static final List<String> FILES = Arrays.asList(
            "document.pdf", "image.jpg", "script.py", "style.css",
            "presentation.pptx", "notes.txt", "backup.tar.gz",
            "video.mp4", "audio.mp3", "code.java", "report.pdf",
            "screenshot.png", "long_filename_with_details.jpg",
            "configuration.yaml", "data.csv", "very_long_filename_for_testing_purposes.pdf"
    );

    public static void main(String[] args) {
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {

            long start = System.nanoTime();
            String result = forkJoinPool.invoke(new LongestFileNameTask(FILES, 0, FILES.size()));
            long end = System.nanoTime();
            System.out.println("Result of Longest File Name Task:: %s, time :: %d".formatted(result, end - start));

            forkJoinPool.invoke(new CapitalizeAction(FILES, 0, FILES.size()));
            System.out.println("Result of Capitalize Action:: %s".formatted(FILES));
        }
    }

    /**
     * A RecursiveTask that finds the longest filename in a list.
     * Splits the list into smaller chunks and processes them in parallel,
     * returning the result (longest filename).
     */
    private static final class LongestFileNameTask extends RecursiveTask<String> {
        private final List<String> filenames;
        private final int from;
        private final int to;
        private static final int THRESHOLD = 5; // Size at which to stop splitting

        public LongestFileNameTask(List<String> filenames, int from, int to) {
            this.filenames = filenames;
            this.from = from;
            this.to = to;
        }

        /**
         * The main computation method. If the chunk is small enough, processes it directly;
         * otherwise, splits it into two subtasks and combines their results.
         *
         * @return the longest filename in the assigned chunk
         */
        @Override
        protected String compute() {
            String longest = "";
            if ((to - from) <= THRESHOLD) {
                // Directly find the longest filename in small chunk
                for (int i = from; i < to; i++) {
                    String current = filenames.get(i);
                    if (current.length() > longest.length()) {
                        longest = current;
                    }
                }
                return longest;
            } else {
                // Split chunk into two parts
                int mid = from + (to - from) / 2;
                LongestFileNameTask left = new LongestFileNameTask(filenames, from, mid);
                LongestFileNameTask right = new LongestFileNameTask(filenames, mid, to);

                // Fork left task to run in parallel
                left.fork();
                // Compute right task in current thread (optimization)
                String rightResult = right.compute();
                // Wait for left task to finish and get its result
                String leftResult = left.join();

                /* Alternative approach (less efficient):
                   left.fork(); right.fork();
                   String leftResult = left.join();
                   String rightResult = right.join();
                   - More overhead, current thread idle
                 */
                // Return the longer of the two results
                return rightResult.length() > leftResult.length() ? rightResult : leftResult;
            }
        }
    }

    /**
     * A RecursiveAction that capitalizes filenames in a list in place.
     * Splits the list into smaller chunks and processes them in parallel,
     * modifying the original list without returning a result.
     */
    private static final class CapitalizeAction extends RecursiveAction {
        private final List<String> list;
        private final int from;
        private final int to;
        private static final int THRESHOLD = 5; // Size at which to stop splitting
        
        public CapitalizeAction(List<String> list, int from, int to) {
            this.list = list;
            this.from = from;
            this.to = to;
        }

        /**
         * The main computation method. If the chunk is small enough, capitalizes directly;
         * otherwise, splits it into two subtasks to process in parallel.
         */
        @Override
        protected void compute() {
            if ((to - from) <= THRESHOLD) {
                // Capitalize small chunk directly
                for (int i = from; i < to; i++) {
                    list.set(i, list.get(i).toUpperCase());
                }
            } else {
                // Split chunk into two parts
                int mid = from + (to - from) / 2;
                CapitalizeAction left = new CapitalizeAction(list, from, mid);
                CapitalizeAction right = new CapitalizeAction(list, mid, to);
                // Execute both subtasks in parallel
                invokeAll(left, right);
            }
        }
    }
}