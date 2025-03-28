package com.abbos.maang.core.io_nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A utility class for traversing file systems and performing operations on files using the NIO.2 API.
 */
public class FileVisitor {
    private static final String PATH = "/Users/mac/Desktop/IntelliJ IDEA/MAANG/src/main/java/com/abbos/maang/core/io_nio";

    public static void main(String[] args) {
        Path path = Paths.get(PATH);
        try {
            Files.walkFileTree(path, new FindJavaVisitor());
        } catch (IOException ignore) {
        }
    }

    /**
     * A custom file visitor implementation that identifies and processes Java source files during directory traversal.
     * Extends {@link SimpleFileVisitor} to override specific behavior for file visits.
     */
    private static final class FindJavaVisitor extends SimpleFileVisitor<Path> {
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toString().endsWith(".java")) {
                System.out.println(file.getFileName());
            }
            return FileVisitResult.CONTINUE;
        }
    }
}