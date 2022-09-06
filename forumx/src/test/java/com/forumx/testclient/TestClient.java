package com.forumx.testclient;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumx.controller.form.TopicoForm;

public class TestClient {
	private static final String TOKEN_MODERADOR = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJBUEkgZG8gRsOzcnVtIFgiLCJzdWIiOiIyIiwiaWF0IjoxNjYyNDY3MzM0LCJleHAiOjE2NjI1NTM3MzR9.X-9sfNno2OSuj6CjsZ2JZxo5r25CSU4IBSgTQBM6iB94CgHUPS-rDcJkbVuyY-xjSmG6eXfLj8fWqXj82AEDkQ";

	public static void main(String[] args) throws Exception {
		post();

		get();

	}

	private static void post() throws Exception {
		String topico = new ObjectMapper().writeValueAsString(new TopicoForm("Dúvida Sobre Postman", "Não está autenticando", "Spring Boot"));
		StringEntity stringEntity = new StringEntity(topico, ContentType.APPLICATION_JSON);

		try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpPost httppost = new HttpPost("http://localhost:8080/topicos");
			httppost.setHeader("Authorization", "Bearer " + TOKEN_MODERADOR);

			httppost.setEntity(stringEntity);

			System.out.println("Executing request " + httppost.getMethod() + " " + httppost.getUri());

			String resultado = httpclient.execute(httppost, new MyResponseHandler());

			System.out.println(resultado);
		}
	}

	private static void get() throws IOException {

		String conteudo = Request.get("http://localhost:8080/topicos/1436")

				.setHeader("Authorization", "Bearer " + TOKEN_MODERADOR)

				.setHeader("Accept", "application/json")

				.execute().returnContent().asString();

		System.out.println("HttpClient:" + conteudo);
	}
}
