package com.forumx.controller.form;

import org.hibernate.validator.constraints.Length;

import com.forumx.modelo.Curso;
import com.forumx.modelo.Topico;
import com.forumx.repository.CursoRepository;

public class TopicoForm {

	@Length(min = 5, message = "Mínimo de 5 caracteres")
	private String titulo;
	@Length(min = 5)
	private String mensagem;
	@Length(min = 5)
	private String nomeCurso;

	public TopicoForm() {
	}

	public TopicoForm(String titulo, String mensagem, String nomeCurso) {
		this.titulo = titulo;
		this.mensagem = mensagem;
		this.nomeCurso = nomeCurso;
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

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public Topico converter(CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(this.nomeCurso);
		return new Topico(this.titulo, this.mensagem, curso);
	}

}
