package com.forumx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forumx.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String username);

}
