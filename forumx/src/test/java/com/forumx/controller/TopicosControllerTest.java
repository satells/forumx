package com.forumx.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumx.base.BaseTest;
import com.forumx.controller.form.TopicoForm;
import com.forumx.repository.CursoRepository;
import com.forumx.repository.TopicoRepository;

class TopicosControllerTest extends BaseTest {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@Test
	void test_get_withou_parameters() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/topicos");

		mockMvc.perform(requestBuilder)

				.andExpect(status().is2xxSuccessful())

				.andExpect(content().contentType("application/json"))

		;
	}

	@Test
	void test_post() throws Exception {

		String topico = new ObjectMapper().writeValueAsString(new TopicoForm("Dúvida Postman", "Texto da mensagem", "Spring Boot"));

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.post("/topicos")

				.accept("application/json;charset=UTF-8")

				.content(topico)

				.contentType("application/json;charset=UTF-8");

		mockMvc.perform(requestBuilder)

				.andExpect(status().isCreated())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(MockMvcResultMatchers.redirectedUrlPattern("http://**/topicos/*"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.id", notNullValue()))

				.andExpect(jsonPath("$.titulo", is("Dúvida Postman")))

				.andExpect(jsonPath("$.mensagem", is("Texto da mensagem")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()));

	}

	@Test
	void test_post_with_error_on_validation() throws Exception {

		String topico = new ObjectMapper().writeValueAsString(new TopicoForm("", "Texto da mensagem", "Spring Boot"));

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.post("/topicos")

				.accept("application/json;charset=UTF-8")

				.header("Accept-Language", "pt-BR")

				.content(topico)

				.contentType("application/json;charset=UTF-8");

		;

		mockMvc.perform(requestBuilder)

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(status().is4xxClientError())

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$[0].campo", is("titulo")))

				.andExpect(jsonPath("$[0].erro", is("Mínimo de 5 caracteres")))

		;

	}

}
