package com.example.demo.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {

    private PathUtil() {
        // utils
    }

    public static Path userDir() {
        String userDir = System.getProperty("user.dir");
        return Paths.get(userDir);
    }

}
