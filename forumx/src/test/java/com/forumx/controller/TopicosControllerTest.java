
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
	private static final String TOKEN_USUARIO = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJBUEkgZG8gRsOzcnVtIFgiLCJzdWIiOiIyIiwiaWF0IjoxNjYyNDY3MzM0LCJleHAiOjE2NjI1NTM3MzR9.X-9sfNno2OSuj6CjsZ2JZxo5r25CSU4IBSgTQBM6iB94CgHUPS-rDcJkbVuyY-xjSmG6eXfLj8fWqXj82AEDkQ";
	private static final String TOKEN_MODERADOR = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJBUEkgZG8gRsOzcnVtIFgiLCJzdWIiOiIyIiwiaWF0IjoxNjYyNDY3MzM0LCJleHAiOjE2NjI1NTM3MzR9.X-9sfNno2OSuj6CjsZ2JZxo5r25CSU4IBSgTQBM6iB94CgHUPS-rDcJkbVuyY-xjSmG6eXfLj8fWqXj82AEDkQ";

	@Test
	void test_delete() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.delete("/topicos/{id}", 3)

				.header("Authorization", "Bearer " + TOKEN_MODERADOR)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())

		;

		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders

				.delete("/topicos/{id}", "789789")

				.accept("application/json")

				.contentType("application/json")

				.header("Authorization", "Bearer " + TOKEN_MODERADOR)

		;

		mockMvc.perform(requestBuilder2)

				.andExpect(MockMvcResultMatchers.status().is(404))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.campo", is("C??digo 789789")))

				.andExpect(jsonPath("$.erro", is("N??o encontrado")))

		;

	}

	@Test
	void test_get_withou_parameters() throws Exception {

		MockHttpServletRequestBuilder requestBuilderSemParametro = MockMvcRequestBuilders

				.get("/topicos?page=1&size=50&sort=id,asc&sort=dataCriacao,asc")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

				.accept("application/json;charset=UTF-8")

		;
		MockHttpServletRequestBuilder requestBuilderComParametro = MockMvcRequestBuilders

				.get("/topicos?nomeCurso=S&page=0&size=50&sort=id,desc")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

				.accept("application/json;charset=UTF-8")

		;

		mockMvc.perform(requestBuilderComParametro)

				.andExpect(status().is2xxSuccessful())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", Matchers.notNullValue()))

				.andExpect(jsonPath("$.content[0].id", Matchers.notNullValue()))

				.andExpect(jsonPath("$.content[0].titulo", is("D??vida 1429")))

				.andExpect(jsonPath("$.content[0].mensagem", is("Projeto n??o compila 1429")))

				.andExpect(jsonPath("$.content[0].dataCriacao", notNullValue()));
		;
		mockMvc.perform(requestBuilderSemParametro)

				.andExpect(status().is2xxSuccessful())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", Matchers.notNullValue()))

				.andExpect(jsonPath("$.content[0].id", Matchers.notNullValue()))

				.andExpect(jsonPath("$.content[0].titulo", is("D??vida 49")))

				.andExpect(jsonPath("$.content[0].mensagem", is("Projeto n??o compila 49")))

				.andExpect(jsonPath("$.content[0].dataCriacao", notNullValue()));
		;

	}

	@Test
	void test_post() throws Exception {

		String topico = new ObjectMapper().writeValueAsString(new TopicoForm("D??vida Postman", "Texto da mensagem", "Spring Boot"));

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.post("/topicos")

				.accept("application/json")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

				.content(topico)

				.contentType("application/json")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(status().isCreated())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(MockMvcResultMatchers.redirectedUrlPattern("http://**/topicos/*"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.id", notNullValue()))

				.andExpect(jsonPath("$.titulo", is("D??vida Postman")))

				.andExpect(jsonPath("$.mensagem", is("Texto da mensagem")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()));

	}

	@Test
	void test_post_with_error_on_validation() throws Exception {

		String topico = new ObjectMapper().writeValueAsString(new TopicoForm("", "Texto da mensagem", "Spring Boot"));

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.post("/topicos")

				.accept("application/json")

				.header("Accept-Language", "pt-BR")

				.content(topico)

				.contentType("application/json")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(status().is4xxClientError())

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$[0].campo", is("titulo")))

				.andExpect(jsonPath("$[0].erro", is("M??nimo de 5 caracteres")))

		;

	}

	@Test
	public void test_get_detalhar() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.get("/topicos/{id}", 1)

				.accept("application/json;charset=UTF-8")

				.contentType("application/json;charset=UTF-8")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;
		MockHttpServletRequestBuilder requestBuilderNaoEncontrado = MockMvcRequestBuilders

				.get("/topicos/{id}", -1)

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(status().isOk())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.id", is(1)))

				.andExpect(jsonPath("$.titulo", is("D??vida")))

				.andExpect(jsonPath("$.mensagem", is("Erro ao criar projeto")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()))

				.andExpect(jsonPath("$.nomeAutor", is("Aluno")))

				.andExpect(jsonPath("$.status", is("NAO_RESPONDIDO")))

				.andExpect(jsonPath("$.respostas", notNullValue()))

		;
		mockMvc.perform(requestBuilderNaoEncontrado)

				.andExpect(status().is(404))

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.campo", is("C??digo -1")))

				.andExpect(jsonPath("$.erro", is("N??o encontrado")))

		;

	}

	@Test
	void test_put_atualizacao() throws Exception {

		String atualizarTopico = new ObjectMapper().writeValueAsString(new AtualizacaoTopicoForm("Atualizado", "Mensagem nova"));

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.put("/topicos/{id}", 2)

				.accept("application/json")

				.contentType("application/json")

				.content(atualizarTopico)

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(MockMvcResultMatchers.status().isOk())

				.andExpect(jsonPath("$.id", is(2)))

				.andExpect(jsonPath("$.titulo", is("Atualizado")))

				.andExpect(jsonPath("$.mensagem", is("Mensagem nova")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()));

		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders

				.get("/topicos/{id}", 2)

				.accept("application/json")

				.contentType("application/json")

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;

		mockMvc.perform(requestBuilder2)

				.andExpect(status().isOk())

				.andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.id", is(2)))

				.andExpect(jsonPath("$.titulo", is("Atualizado")))

				.andExpect(jsonPath("$.mensagem", is("Mensagem nova")))

				.andExpect(jsonPath("$.dataCriacao", notNullValue()))

				.andExpect(jsonPath("$.nomeAutor", is("Aluno")))

				.andExpect(jsonPath("$.status", is("NAO_RESPONDIDO")))

				.andExpect(jsonPath("$.respostas", notNullValue()))

		;

		MockHttpServletRequestBuilder requestBuilder3 = MockMvcRequestBuilders

				.put("/topicos/{id}", "456464")

				.accept("application/json")

				.contentType("application/json")

				.content(atualizarTopico)

				.header("Authorization", "Bearer " + TOKEN_USUARIO)

		;

		mockMvc.perform(requestBuilder3)

				.andExpect(MockMvcResultMatchers.status().is(404))

				.andExpect(jsonPath("$", notNullValue()))

				.andExpect(jsonPath("$.campo", is("C??digo 456464")))

				.andExpect(jsonPath("$.erro", is("N??o encontrado")))

		;

	}

}
