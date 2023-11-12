package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private FileUtil() {
        // utils
    }

    public static String read(Path path) {
        File file = path.toFile();
        if (!file.exists()) {
            throw new RuntimeException("File " + path + " does not exist");
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Path path, String content) {
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeId(String path, String content) {
        try {
            Path path1 = Path.of(path);
            Files.write(path1, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
