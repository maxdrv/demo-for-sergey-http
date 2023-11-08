package com.example.demo.repository;

import com.example.demo.util.FileUtil;
import com.example.demo.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.repository.NextTodoId.nextTodoId;

public class FileTodoRepository {

    private final Path todoFile;

    public FileTodoRepository(Path todoFile) {
        this.todoFile = todoFile;
    }

    public List<Todo> findAll() {
        return readFromFile();
    }

    public Todo save(TodoCreateRequest request) {
        Todo todo = new Todo();
        List<Todo> todos = readFromFile();
        todo.setUserId(1L);
        todo.setId(nextTodoId());
        todo.setTitle(request.getTitle());
        todo.setCompleted(false);
        todos.add(todo);

        String json = JsonUtil.writeValueAsString(todos);
        FileUtil.write(todoFile, json);

        return todo;
    }

    public List<Todo> deleteById(Long id) throws IOException {
        List<Todo> todosAll = readFromFile();
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


    public Todo findByIdOrThrow(long id) {
        readFromFile();

        for (Todo todo : readFromFile()) {
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
        List<Todo> todosAll = readFromFile();
        for (Todo todo : todosAll) {
            if (todo.getId() == id) {
                if (request.getCompleted() != null) {
                    todo.setCompleted(request.getCompleted());
                }
                if (request.getTitle() != null) {
                    todo.setTitle(request.getTitle());
                }
                ObjectMapper mapper2 = new ObjectMapper();
                try {
                    File file = new File("C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json");
                    FileWriter writer = new FileWriter(file);
                    mapper2.writeValue(writer, todosAll);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return todo;
            }
        }
        return null;
    }

    public void deleteAll() {
        FileUtil.write(todoFile, "[]");
    }

    private List<Todo> readFromFile() {
        String json = FileUtil.read(todoFile);
        return JsonUtil.readTodoList(json);
    }

}


