package com.forumx.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration

//https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
//https://spring.io/blog/2019/11/21/spring-security-lambda-dsl
//https://stackoverflow.com/questions/72381114/spring-security-upgrading-the-deprecated-websecurityconfigureradapter-in-spring
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * Configuração de autenticações
	 * 
	 * >>Controle de acesso
	 * 
	 * >>Login
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	}

	/**
	 * Configurações de autorização
	 * 
	 * >> Quem pode acessar URL
	 * 
	 * >> Perfil de acesso
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http

				.authorizeRequests()

				.antMatchers(HttpMethod.GET, "/topicos").permitAll()

				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll();
	}

	/**
	 * Configurações de recursos estáticos
	 * 
	 * 
	 * >> Requisições para arquivos (css, js, imagens, html, etc)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {

	}
}
