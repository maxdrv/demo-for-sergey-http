package com.example.demo.repository;

import com.example.demo.util.FileUtil;
import com.example.demo.util.JsonUtil;
import io.micrometer.common.lang.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;



public class FileTodoRepository {

    private final Path todoFile;

    private final SequenceOnDiskParametrized sequence;

    public FileTodoRepository(Path todoFile, SequenceOnDiskParametrized sequence) {
        this.todoFile = todoFile;
        this.sequence = sequence;
    }

    public List<Todo> findAll(@Nullable Boolean completed, @Nullable String title) {
        List<Todo> jsonAll = readFromFile();
        List<Todo> result = new ArrayList<>();
        for (Todo todo : jsonAll) {
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

    public Todo save(TodoCreateRequest request) {
        Todo todo = new Todo();
        List<Todo> todos = readFromFile();
        todo.setUserId(1L);
        todo.setId(sequence.next());
        todo.setTitle(request.getTitle());
        todo.setCompleted(false);
        todos.add(todo);
        String json = JsonUtil.writeValueAsString(todos);
        FileUtil.write(todoFile, json);

        return todo;
    }

    public List<Todo> deleteById(Long id) {
        List<Todo> todosAll = readFromFile();
        List<Todo> result = new ArrayList<>();
        for (Todo todo : todosAll) {
            if (todo.getId() != id) {
                result.add(todo);
            }
        }
        String json = JsonUtil.writeValueAsString(result);
        FileUtil.write(todoFile, json);

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
                String json = JsonUtil.writeValueAsString(todosAll);
                FileUtil.write(todoFile, json);

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
    public void  IdDelete(){
        sequence.idReset();
    }

}


