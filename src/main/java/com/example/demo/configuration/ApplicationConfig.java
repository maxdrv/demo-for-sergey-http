package com.example.demo.configuration;

import com.example.demo.repository.FileTodoRepository;
import com.example.demo.repository.SequenceOnDiskParametrized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class ApplicationConfig {

    @Value("${file.todo.relative.path}")
    private String todoRelativePath;

    @Value("${file.todoId.relative.path}")
    private String todoIdRelativePath;

    @Bean
    public FileTodoRepository fileTodoRepository() {
        String userDir = System.getProperty("user.dir");
        String todoFilePath = userDir + todoRelativePath;
        Path p = Path.of(todoFilePath);
        String idFile = userDir + todoIdRelativePath;
        SequenceOnDiskParametrized sequence = new SequenceOnDiskParametrized(idFile);
        return new FileTodoRepository(p, sequence);
    }

}
