package com.abbos.maang.core.io_nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Demonstrates reading a file using MappedByteBuffer for efficient memory usage.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-04-01
 */
public class MappedByteBufferExample {
    public static void main(String[] args) {
        File file = new File("src/main/resources/io/test.txt");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) { // r = read only mode or {rw} - read & write
            FileChannel channel = randomAccessFile.getChannel();
            long fileSize = channel.size(); // total size of this file
            long chunkSize = 1024 * 1024; // 1 MB
            for (long position = 0; position < fileSize; position += chunkSize) {
                long remaining = fileSize - position; // bytes left to process
                int sizeToMap = (int) Math.min(chunkSize, remaining); // size of current chunk
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, sizeToMap);
                processBuffer(buffer);
            }
        } catch (IOException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static void processBuffer(MappedByteBuffer buffer) {
        StringBuffer stringBuffer = new StringBuffer();
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            stringBuffer.append((char) b);
        }
        System.out.println(stringBuffer);
    }
}
