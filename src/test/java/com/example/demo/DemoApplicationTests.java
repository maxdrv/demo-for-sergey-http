package com.example.demo;

import com.example.demo.repository.FileTodoRepository;
import com.example.demo.repository.SequenceOnDiskParametrized;
import com.example.demo.repository.TodoCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	public FileTodoRepository todoRepository;


	@Autowired
	MockMvc mockMvc;
	@BeforeEach
	void init1() throws IOException {
		todoRepository.IdDelete();
	}
	@BeforeEach
	void init() throws IOException {
		todoRepository.deleteAll();
	}


	@Test
	void getTodos() throws Exception {
		TodoCreateRequest request = new TodoCreateRequest();
		request.setTitle("something test");
		todoRepository.save(request);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].userId").value(1))
				.andExpect(jsonPath("$.[0].title").value("something test"))
				.andExpect(jsonPath("$.[0].completed").value(false))
				.andExpect(jsonPath("$.[0].id").value(1))
				.andExpect(jsonPath("$.length()").value(1));
	}


}
