package com.abbos.maang.core.io_nio;

import java.io.*;

public class IOExecutor {
    // For java.io streams
    public static <I extends InputStream, O extends OutputStream> void executeStreams(
            IOTask<I, O> task, I input, O output) throws IOException {
        try (I in = input; O out = output) {
            task.processOutput(out);  // First write
            task.processInput(in);    // Then read
        }
    }

    // For java.io readers and writers
    public static <R extends Reader, W extends Writer> void executeReadersWriters(
            IOTask<R, W> task, R reader, W writer) throws IOException {
        try (R rd = reader; W wr = writer) {
            task.processOutput(wr);   // First write
            task.processInput(rd);    // Then read
        }
    }
}
