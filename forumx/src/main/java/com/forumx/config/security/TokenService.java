package com.forumx.config.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.forumx.modelo.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

//https://github.com/jwtk/jjwt
@Service
public class TokenService {

	@Value("${forumx.jwt.expiration}")
	private String expiration;

	@Value("${forumx.jwt.secret}")
	private String secret;

	public boolean isTokenValido(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

			System.out.println("tudo ok");
			return true;
		} catch (Exception e) {
			System.out.println("tudo falso");
			return false;
		}
	}

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();

		Key key2 = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder()

				.setIssuer("API do Fórum X")

				.setSubject(logado.getId().toString())

				.setIssuedAt(hoje)

				.setExpiration(dataExpiracao)

				.signWith(key)

				.compact()

		;
	}

	public Long getIdUsuario(String token) {
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		String subject = jws.getBody().getSubject();

		return Long.parseLong(subject);
	}

}
