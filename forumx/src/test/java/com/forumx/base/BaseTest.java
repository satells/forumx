package com.forumx.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import com.forumx.ForumxApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { ForumxApplication.class })
@AutoConfigureMockMvc
public class BaseTest {
	@Autowired
	protected MockMvc mockMvc;

}
