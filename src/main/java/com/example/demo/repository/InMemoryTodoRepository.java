package com.example.demo.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class InMemoryTodoRepository {

    private long todoSeq = 1L;
    private long subtaskSeq = 1L;

    private List<Todo> list;

    public InMemoryTodoRepository() {
        this.list = new ArrayList<>();

    }

    public List<Todo> findAll() {
        return readJson();
    }

    public Todo findByIdOrThrow(long id) {
        readJson();

        for (Todo todo : this.readJson()) {
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

    public List<Todo> readJson() {
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
                return mapper.readValue(new File(filePath), new TypeReference<List<Todo>>() {
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
        ObjectMapper mapper = new ObjectMapper();
        List<Todo> todos = readJson();
        Todo todo = new Todo();
        todo.setUserId(1L);
        todo.setId(nextTodoId());
        todo.setTitle(request.getTitle());
        int count_Id = todos.size();
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

    public Todo update(long id, TodoUpdateRequest request) {
        Todo founded = findByIdOrThrow(id);

        if (request.getCompleted() != null) {
            founded.setCompleted(request.getCompleted());
        }
        if (request.getTitle() != null) {
            founded.setTitle(request.getTitle());
        }

        return founded;
    }

    private long nextTodoId() {
        List<Todo> todos = readJson();
        return todos.size() + 1;
    }

    private long nextSubtaskId() {

        return subtaskSeq++;
    }

    public List<Todo> deleteTodo(Long id) throws IOException {
        List<Todo> todosAll = readJson();
        List<Todo> result = new ArrayList<>();
        for (int i = 0; i < todosAll.size(); i++) {
            if (todosAll.get(i).getId() != id) {
                result.add(todosAll.get(i));
            }
        }

        ObjectMapper mapper2 = new ObjectMapper();
        try {
            File file = new File("C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json");
            FileWriter writer = new FileWriter(file);
            mapper2.writeValue(writer, result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;

    }


    public List<Todo> filterTodos(@Nullable Boolean completed, @Nullable String title) {
        List<Todo> ischodnick = readJson();
        List<Todo> result = new ArrayList<>();
        for (Todo todo : ischodnick) {
            if (title != null && completed != null) {
                String todoLowerCase = todo.getTitle().toLowerCase();
                String filterLowerCase = title.toLowerCase();
                if (todoLowerCase.contains(filterLowerCase) && todo.isCompleted() == completed) {
                    result.add(todo);
                }
            } else if (title != null) {
                String todoLowercase = todo.getTitle().toLowerCase();
                String filterLowercase = title.toLowerCase();
                if (todoLowercase.contains(filterLowercase)) {
                    result.add(todo);
                }
            } else if (completed != null) {
                if (todo.isCompleted() == completed) {
                    result.add(todo);
                }
            } else {
                result.add(todo);
            }

        }
        return result;
    }
}



