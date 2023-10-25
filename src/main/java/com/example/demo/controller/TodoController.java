package com.example.demo.controller;

import com.example.demo.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TodoController {
    private final String filePath = "C:\\Users\\User\\Desktop\\demo-for-sergey-http\\Todo.json";

    private final InMemoryTodoRepository todoRepository;
    public TodoController(InMemoryTodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }//
    @GetMapping(value = "/v1/tasks")
    public ResponseEntity<List<Todo>> findAllV1(
            @Nullable
            @RequestParam(value = "title", required = false)
            String title,
            @Nullable
            @RequestParam(value = "completed", required = false)
            Boolean completed
    ) {
        List<Todo> todos;
        todos = todoRepository.filterTodos(completed, title);
        return  ResponseEntity.ok(todos);
    }

    @GetMapping("/ping")

    public ResponseEntity<String> ok() {
        HttpStatus code = HttpStatus.OK;
        String body = "BODY";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Custom999", "Max");
        return new ResponseEntity<>(body, headers, code);
    }

    @GetMapping("/v3/tasks")
    public ResponseEntity<String> findAllV1() {
        ObjectMapper mapper = new ObjectMapper();
        List<Todo> todos = todoRepository.findAll();

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(todos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(jsonBody, headers, HttpStatus.OK);
    }


    @GetMapping("/v2/tasks")
    public ResponseEntity<List<Todo>> findAllV2() {
        List<Todo> todos = todoRepository.findAll();
        return new ResponseEntity<>(todos, null, HttpStatus.OK);
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

}
