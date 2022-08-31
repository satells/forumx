package com.forumx.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
@Configuration

//https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
//https://spring.io/blog/2019/11/21/spring-security-lambda-dsl
//https://stackoverflow.com/questions/72381114/spring-security-upgrading-the-deprecated-websecurityconfigureradapter-in-spring
public class SecurityConfiguration {// extends WebSecurityConfigurerAdapter {

	/**
	 * Configuração de autenticações
	 * 
	 * >>Controle de acesso
	 * 
	 * >>Login
	 */

	@Autowired
	private AutenticacaoService autenticacaoService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

//	@Bean
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
//
//	}

	/**
	 * ==========================================================================
	 * Configurações de autorização >>
	 * 
	 * >>Quem pode acessar URL
	 * 
	 * >> Perfil de acesso
	 * 
	 */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http

				.authorizeRequests()

				.antMatchers(HttpMethod.POST, "/auth").permitAll()

				.antMatchers(HttpMethod.GET, "/topicos").permitAll()

				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()

				.anyRequest().authenticated()

				.and().csrf().disable()

				.sessionManagement()

				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

		;

		return http.build();
	}

	/**
	 * ==============================================================================
	 * Configurações de recursos estáticos
	 * 
	 * 
	 * >> Requisições para arquivos (css, js, imagens, html, etc)
	 */
//	@Bean
//	public void configure(WebSecurity web) throws Exception {
//
//	}

}
