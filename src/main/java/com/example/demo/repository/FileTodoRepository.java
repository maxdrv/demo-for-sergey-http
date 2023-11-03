package com.example.demo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.demo.repository.NextTodoId.nextTodoId;

@Component
public class FileTodoRepository {

    private long todoSeq = 1L;
    private long subtaskSeq = 1L;
    private List<Todo> list;

    public FileTodoRepository() {
        this.list = new ArrayList<>();
    }

    public static List<Todo> readJson() {
        ObjectMapper mapper = new ObjectMapper();
        String filePath = "C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json";
        try {
            List<Todo> todos = new ArrayList<>();
            File file = new File(filePath);
            if (file.exists()) {
                String result = Files.readString(file.toPath());
                if (result.isBlank()) {
                    return todos;
                }
                return mapper.readValue(new File(filePath), new TypeReference<>() {
                });

            } else {
                return todos;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Todo save(TodoCreateRequest request) throws IOException {
        Todo todo = new Todo();
        List<Todo> todos = readJson();
        todo.setUserId(1L);
        todo.setId(nextTodoId());
        todo.setTitle(request.getTitle());
        todo.setCompleted(false);
        todos.add(todo);
        ObjectMapper mapper2 = new ObjectMapper();
        try {
            File file = new File("C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json");
            FileWriter writer = new FileWriter(file);
            mapper2.writeValue(writer, todos);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return todo;
    }

    public List<Todo> deleteById(Long id) throws IOException {
        List<Todo> todosAll = readJson();
        List<Todo> result = new ArrayList<>();
        for (Todo todo : todosAll) {
            if (todo.getId() != id) {
                result.add(todo);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json");
            FileWriter writer = new FileWriter(file);
            mapper.writeValue(writer, result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Todo findByIdOrThrow(long id) {
        readJson();

        for (Todo todo : readJson()) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        throw new RuntimeException("todo not found by id " + id);
    }

    public SubTask findByTodoIdAndTaskIdOrThrow(long todoId, long subtaskId) {
        Todo todo = findByIdOrThrow(todoId);

        if (todo.getSubtasks() == null) {
            throw new RuntimeException("subtask not found by subtask id " + subtaskId);
        }

        for (SubTask subTask : todo.getSubtasks()) {
            if (subTask.getId() == subtaskId) {
                return subTask;
            }
        }
        throw new RuntimeException("subtask not found by subtask id " + subtaskId);
    }

    public Todo update(long id, TodoUpdateRequest request) {
        Todo founded = findByIdOrThrow(id);
        List<Todo> todosAll = readJson();

        if (request.getCompleted() != null) {
            founded.setCompleted(request.getCompleted());
        }
        if (request.getTitle() != null) {
            founded.setTitle(request.getTitle());
            int index = -1;
            for (int i = 0; i < todosAll.size(); i++) {
                if (todosAll.get(i).getId() == founded.getId()) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                todosAll.set(index, founded);
            }

            ObjectMapper mapper2 = new ObjectMapper();
            try {
                File file = new File("C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json");
                FileWriter writer = new FileWriter(file);
                mapper2.writeValue(writer, todosAll);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return founded;

    }
}

