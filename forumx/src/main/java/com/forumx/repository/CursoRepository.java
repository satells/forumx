package com.forumx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forumx.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Curso findByNome(String nomeCurso);

}
