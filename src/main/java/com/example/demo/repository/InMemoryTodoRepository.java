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
    public List<Todo> findAllByFilter(@Nullable String title) {
        if (title == null) {
            return this.list;
        }
        List<Todo> result = new ArrayList<>();
        for (Todo todo : this.list) {
            String todoLowercase = todo.getTitle().toLowerCase();
            String filterLowercase = title.toLowerCase();
            if (todoLowercase.contains(filterLowercase)) {
                result.add(todo);
            }
        }
        return result;
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
            File file = new File(filePath);
            if(file.exists()){
                String result =Files.readString(file.toPath());
                if(result.isBlank()){
                    List <Todo> todos = new ArrayList<>();
                    return  todos;
                }
                return mapper.readValue(new File(filePath), new TypeReference<List<Todo>>() {});

            }

            else {
                List <Todo> todos = new ArrayList<>();
                return  todos;
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
        int count_Id=todos.size();
        todo.setCompleted(false);

        //File file2 = new File("C:\\Users\\User\\Desktop\\demo-for-sergey-http-master2\\idSave.txt");
        //FileWriter writer2 = new FileWriter("C:\\Users\\User\\Desktop\\demo-for-sergey-http-master2\\idSave.txt");

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
        return todos.size()+1;
    }

    private long nextSubtaskId() {

        return subtaskSeq++;
    }



}
