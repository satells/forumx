package com.forumx.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumx.base.BaseTest;

class AutenticacaoControllerTest extends BaseTest {

	class UsuarioTeste {
		private String email;
		private String senha;

		public UsuarioTeste(String email, String senha) {
			this.email = email;
			this.senha = senha;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

	}

	@Test
	void testLogin() throws Exception {

		String login = new ObjectMapper().writeValueAsString(new UsuarioTeste("aluno@email.com", "123456"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders

				.post("/auth")

				.contentType("application/json;charset=UTF-8")

				.accept("application/json;charset=UTF-8")

				.content(login)

		;

		mockMvc.perform(requestBuilder)

				.andExpect(MockMvcResultMatchers.status().isOk())

		;
	}

}
