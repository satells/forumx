package com.forumx.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.forumx.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	List<Topico> findByTitulo(String nomeCurso);

	Page<Topico> findByCursoNomeStartingWithIgnoreCase(String nomeCurso, Pageable paginacao);

	Topico getTopicoById(Long id);

}
