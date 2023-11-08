package com.example.demo.configuration;

import com.example.demo.repository.FileTodoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class ApplicationConfig {

    @Value("${file.todo.relative.path}")
    private String relativePath;

    @Bean
    public FileTodoRepository fileTodoRepository() {
        String userDir = System.getProperty("user.dir");
        String todoFilePath = userDir + relativePath;
        Path p = Path.of(todoFilePath);
        return new FileTodoRepository(p);
    }

}
