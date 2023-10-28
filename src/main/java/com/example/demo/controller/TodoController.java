package com.example.demo.controller;

import com.example.demo.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class TodoController {

    private final FileTodoRepository todoRepository;

    public TodoController(FileTodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping(value = "/v1/tasks")
    public ResponseEntity<List<Todo>> findAll(
            @Nullable
            @RequestParam(value = "title", required = false)
            String title,
            @Nullable
            @RequestParam(value = "completed", required = false)
            Boolean completed
    ) {
        List<Todo> todos = todoRepository.filterTodos(completed, title);
        return ResponseEntity.ok(todos);
    }

    @GetMapping(value = "/v1/tasks/{id}")
    public ResponseEntity<Todo> findById(@PathVariable("id") long id) {
        Todo founded = todoRepository.findByIdOrThrow(id);
        return ResponseEntity.ok(founded);
    }

    @GetMapping(value = "/v1/tasks/{todoId}/sub/{subTaskId}")
    public ResponseEntity<SubTask> findByTodoIdAndTaskId(
            @PathVariable("todoId") long todoId,
            @PathVariable("subTaskId") long subTaskId
    ) {
        SubTask founded = todoRepository.findByTodoIdAndTaskIdOrThrow(todoId, subTaskId);
        return ResponseEntity.ok(founded);
    }

    @PostMapping(value = "/v1/tasks")
    public ResponseEntity<Todo> save(@RequestBody TodoCreateRequest request) throws IOException {
        Todo saved = todoRepository.save(request);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(value = "/v1/tasks/{id}")
    public ResponseEntity<Todo> update(
            @PathVariable("id") long id,
            @RequestBody TodoUpdateRequest request
    ) {
        Todo updated = todoRepository.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/v1/tasks/{id}")
    public ResponseEntity<List<Todo>> deleteById(@PathVariable("id") long id) throws IOException {
        List<Todo> todos = todoRepository.deleteById(id);
        return ResponseEntity.ok(todos);
    }

}
