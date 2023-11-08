package com.example.demo.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NextTodoId {
    public static long nextTodoId() {
        File file = new File("Id.txt");
        long number = readNumberFromFile(file);
        number++;
        writeNumberToFile(file, (int) number);
        return number;
    }

    private static long readNumberFromFile(File file) {
        long number = 0;

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextInt()) {
                number = scanner.nextLong();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }
    private static void writeNumberToFile(File file, int number) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(Integer.toString(number));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
