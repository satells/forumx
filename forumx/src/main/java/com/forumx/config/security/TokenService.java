package com.forumx.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.forumx.modelo.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forumx.jwt.expiration}")
	private String expiration;

	@Value("${forumx.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();

		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder()

				.setIssuer("API do Fórum X")

				.setSubject(logado.getId().toString())

				.setIssuedAt(hoje)

				.setExpiration(dataExpiracao)

				.signWith(SignatureAlgorithm.HS256, secret)

				.compact()

		;
	}

}
