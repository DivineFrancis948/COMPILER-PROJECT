package com.compiler.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.compiler.auth.jwt.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig 
{
	@Autowired
	private  JwtRequestFilter JwtAuthFilter;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.requestMatchers("/register/**").permitAll()
		.requestMatchers("/dashboard/**").permitAll()
		.requestMatchers("/swagger-ui/index.html").permitAll()
		.requestMatchers("/api/**").hasAnyAuthority("ADMIN")
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(JwtAuthFilter,UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	

}

