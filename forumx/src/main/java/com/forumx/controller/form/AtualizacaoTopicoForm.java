package com.forumx.controller.form;

import org.hibernate.validator.constraints.Length;

import com.forumx.modelo.Topico;
import com.forumx.repository.TopicoRepository;

public class AtualizacaoTopicoForm {
	@Length(min = 5, message = "Mínimo de 5 caracteres")
	private String titulo;
	@Length(min = 5)
	private String mensagem;

	public AtualizacaoTopicoForm() {
	}

	public AtualizacaoTopicoForm(@Length(min = 5, message = "Mínimo de 5 caracteres") String titulo, @Length(min = 5) String mensagem) {
		this.titulo = titulo;
		this.mensagem = mensagem;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository topicoRepository) {
		Topico topico = topicoRepository.findById(id).get();

		topico.setTitulo(this.titulo);
		topico.setMensagem(this.mensagem);

		return topico;
	}

}
