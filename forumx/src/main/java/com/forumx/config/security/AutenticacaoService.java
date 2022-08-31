package com.forumx.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.forumx.modelo.Usuario;
import com.forumx.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(username);
		if (usuarioOptional.isPresent()) {
			return usuarioOptional.get();
		}

		throw new UsernameNotFoundException("E-mail não encontrado: " + username);
	}

}
