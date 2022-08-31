package com.forumx.controller.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.forumx.modelo.Topico;

public class TopicoDto {
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao = LocalDateTime.now();

	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();

	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public static Page<TopicoDto> converter(Page<Topico> topicos) {

//		return topicos.stream().map(TopicoDto::new).collect(Collectors.toList());
		return topicos.map(TopicoDto::new);
	}

}
