package com.forumx.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumx.base.BaseTest;
import com.forumx.controller.form.AtualizacaoTopicoForm;
import com.forumx.controller.form.TopicoForm;

class TopicosControllerTest extends BaseTest {

	@Test
	void test_get_withou_parameters() throws Exception {

		MockHttpServletRequestBuilder requestBuilderSemParametro = MockMvcRequestBuilders

				.get("/topicos")

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8");

		;
		MockHttpServletRequestBuilder requestBuilderComParametro = MockMvcRequestBuilders

				.get("/topicos?nomeCurso=S")

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8");

		;

		mockMvc.perform(requestBuilderComParametro)

				.andExpect(status().is2xxSuccessful())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", Matchers.notNullValue()))

				.andExpect(jsonPath("$[0].id", Matchers.notNullValue()))

				.andExpect(jsonPath("$[0].titulo", is("Dúvida")))

				.andExpect(jsonPath("$[0].mensagem", is("Erro ao criar projeto")))

				.andExpect(jsonPath("$[0].dataCriacao", notNullValue()));
		;
		mockMvc.perform(requestBuilderSemParametro)

				.andExpect(status().is2xxSuccessful())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", Matchers.notNullValue()))

				.andExpect(jsonPath("$[0].id", Matchers.notNullValue()))

				.andExpect(jsonPath("$[0].titulo", is("Dúvida")))

				.andExpect(jsonPath("$[0].mensagem", is("Erro ao criar projeto")))

				.andExpect(jsonPath("$[0].dataCriacao", notNullValue()));
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

	@Test
	public void test_get_detalhar() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.get("/topicos/{id}", 1)

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8")

		;
		MockHttpServletRequestBuilder requestBuilderNaoEncontrado = MockMvcRequestBuilders

				.get("/topicos/{id}", -1)

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8")

		;

		mockMvc.perform(requestBuilder)

				.andExpect(status().isOk())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.id", is(1)))

				.andExpect(jsonPath("$.titulo", is("Dúvida")))

				.andExpect(jsonPath("$.mensagem", is("Erro ao criar projeto")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()))

				.andExpect(jsonPath("$.nomeAutor", is("Aluno")))

				.andExpect(jsonPath("$.status", is("NAO_RESPONDIDO")))

				.andExpect(jsonPath("$.respostas", notNullValue()))

		;
		mockMvc.perform(requestBuilderNaoEncontrado)

				.andExpect(status().is4xxClientError())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.campo", is("Código -1")))

				.andExpect(jsonPath("$.erro", is("Não encontrado")))

		;

	}

	@Test
	void test_put_atualizacao() throws Exception {

		String atualizarTopico = new ObjectMapper().writeValueAsString(new AtualizacaoTopicoForm("Atualizado", "Mensagem nova"));

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.put("/topicos/{id}", 3)

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8")

				.content(atualizarTopico)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(MockMvcResultMatchers.status().isOk())

				.andExpect(jsonPath("$.id", is(3)))

				.andExpect(jsonPath("$.titulo", is("Atualizado")))

				.andExpect(jsonPath("$.mensagem", is("Mensagem nova")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()));

		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders

				.get("/topicos/{id}", 3)

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8")

		;

		mockMvc.perform(requestBuilder2)

				.andExpect(status().isOk())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.id", is(3)))

				.andExpect(jsonPath("$.titulo", is("Atualizado")))

				.andExpect(jsonPath("$.mensagem", is("Mensagem nova")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()))

				.andExpect(jsonPath("$.nomeAutor", is("Aluno")))

				.andExpect(jsonPath("$.status", is("NAO_RESPONDIDO")))

				.andExpect(jsonPath("$.respostas", notNullValue()))

		;

	}

}
