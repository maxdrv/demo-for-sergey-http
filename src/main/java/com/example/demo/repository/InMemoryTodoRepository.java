package com.example.demo.repository;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryTodoRepository {

    private long todoSeq = 1L;
    private long subtaskSeq = 1L;

    private List<Todo> list;

    public InMemoryTodoRepository() {
        this.list = new ArrayList<>();

        Todo todo1 = new Todo();
        todo1.setUserId(1L);
        todo1.setId(nextTodoId());
        todo1.setTitle("delectus aut autem");
        todo1.setCompleted(false);

        SubTask sub1 = new SubTask();
        sub1.setId(nextSubtaskId());
        sub1.setTitle("sub1: delectus aut autem");
        sub1.setCompleted(false);

        SubTask sub2 = new SubTask();
        sub2.setId(nextSubtaskId());
        sub2.setTitle("sub2: delectus aut autem");
        sub2.setCompleted(false);

        todo1.setSubtasks(List.of(sub1, sub2));


        Todo todo2 = new Todo();
        todo2.setUserId(1L);
        todo2.setId(nextTodoId());
        todo2.setTitle("quis ut nam facilis et officia qui");
        todo2.setCompleted(false);

        Todo todo3 = new Todo();
        todo3.setUserId(1L);
        todo3.setId(nextTodoId());
        todo3.setTitle("fugiat veniam minus");
        todo3.setCompleted(true);

        this.list.add(todo1);
        this.list.add(todo2);
        this.list.add(todo3);
    }

    public List<Todo> findAll() {
        return this.list;
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
        for (Todo todo : this.list) {
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

    public Todo save(TodoCreateRequest request) {
        Todo todo = new Todo();
        todo.setUserId(1L);
        todo.setId(nextTodoId());
        todo.setTitle(request.getTitle());
        todo.setCompleted(false);
        this.list.add(todo);
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
        return todoSeq++;
    }

    private long nextSubtaskId() {
        return subtaskSeq++;
    }


}
