package com.example.demo.repository;

import jakarta.annotation.Nullable;

public class TodoUpdateRequest {

    @Nullable
    private String title;

    @Nullable
    private Boolean completed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
