package com.forumx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forumx.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	List<Topico> findByTitulo(String nomeCurso);

	List<Topico> findByCursoNomeStartingWithIgnoreCase(String nomeCurso);

	Topico getTopicoById(Long id);

}
