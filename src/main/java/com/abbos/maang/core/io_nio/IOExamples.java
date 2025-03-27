package com.abbos.maang.core.io_nio;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Aliabbos Ashurov
 * @since 20/March/2025  14:48
 */
public class IOExamples {
    private static final String EXAMPLE_OUTPUT = "Hello from FileIO";

    public static void main(String[] args) throws IOException {
        File path = new File("src/main/resources/io/test.txt");
        //runReadersWriters(path);
        runBufferedReaderWriter(path);
    }

    private static void runReadersWriters(File path) throws IOException {
        FileReaderWriterTask fileReaderWriterTask = new FileReaderWriterTask();
        try (FileReader reader = new FileReader(path);
             FileWriter writer = new FileWriter(path)) {
            IOExecutor.executeReadersWriters(fileReaderWriterTask, reader, writer);
        }
    }

    private static void runExecuteStreams(File path) throws IOException {
        FileIOTask fileIOTask = new FileIOTask();
        // Use the same file for both input and output
        try (FileInputStream in = new FileInputStream(path);
             FileOutputStream out = new FileOutputStream(path)) {
            IOExecutor.executeStreams(fileIOTask, in, out);
        }
    }

    private static void runBufferedReaderWriter(File path) throws IOException {
        BufferedStreamTask bufferedStreamTask = new BufferedStreamTask();

        try (BufferedReader reader = new BufferedReader(new FileReader(path));
             BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            IOExecutor.executeReadersWriters(bufferedStreamTask, reader, writer);
        }

    }

    private static final class BufferedStreamTask implements IOTask<BufferedReader, BufferedWriter> {
        @Override
        public void processInput(BufferedReader input) throws IOException {
            System.out.println("Reading (from BufferedReader)......");
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println("Line read: " + line);
            }
            System.out.println("Finished reading (reached end of file)");
            System.out.println("..................................");
        }

        @Override
        public void processOutput(BufferedWriter output) throws IOException {
            System.out.println("Writing (from FileWriter)......");
            output.write(EXAMPLE_OUTPUT);
            output.flush(); // Ensure data is written
            System.out.println("Wrote: " + EXAMPLE_OUTPUT.replace("\n", "\\n"));
            System.out.println("..................................");
        }
    }

    private static final class FileIOTask implements IOTask<FileInputStream, FileOutputStream> {
        @Override
        public void processInput(FileInputStream input) throws IOException {
            System.out.println("Reading (from FileInputStream)......");
            byte[] buffer = new byte[1024]; // 1KB buffer
            int bytesRead;
            StringBuilder content = new StringBuilder();
            while ((bytesRead = input.read(buffer)) != -1) {
                content.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
            System.out.println("Read content: " + content);
        }

        @Override
        public void processOutput(FileOutputStream output) throws IOException {
            System.out.println("Writing (from FileOutputStream)......");
            output.write(EXAMPLE_OUTPUT.getBytes(StandardCharsets.UTF_8));
            output.flush(); // Ensure data is written before reading
            System.out.println("Wrote: " + EXAMPLE_OUTPUT);
        }
    }

    private static final class FileReaderWriterTask implements IOTask<FileReader, FileWriter> {
        @Override
        public void processInput(FileReader input) throws IOException {
            System.out.println("Reading (from FileReader)......");
            char[] buffer = new char[1024]; // 1KB buffer for efficiency
            int charsRead;
            StringBuilder content = new StringBuilder();
            while ((charsRead = input.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }
            System.out.println("Read content: " + content);
            System.out.println("..................................");
        }

        @Override
        public void processOutput(FileWriter output) throws IOException {
            System.out.println("Writing (from FileWriter)......");
            output.write(EXAMPLE_OUTPUT);
            output.flush(); // Ensure data is written before reading
            System.out.println("Wrote: " + EXAMPLE_OUTPUT);
            System.out.println("..................................");
        }
    }
}