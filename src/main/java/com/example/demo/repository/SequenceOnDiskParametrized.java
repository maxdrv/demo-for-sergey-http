package com.example.demo.repository;

import com.example.demo.util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class SequenceOnDiskParametrized {
    private final String path;

    public SequenceOnDiskParametrized(String filePath) {
        this.path = filePath;
    }

    public int next() {
        File file = new File(path);
        int next = 1;
        if(file.exists()){
            try {
                String content = Files.readString(file.toPath());
                if (!content.isEmpty()) {
                    int tmp = Integer.parseInt(content);
                    tmp++;
                    next = tmp;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Integer.toString(next));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return next;

    } public int idReset(){
        File file = new File(path);
        int next = 0;
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Integer.toString(next));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return next;

    }


}