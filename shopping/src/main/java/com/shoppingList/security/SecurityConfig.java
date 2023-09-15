package com.shoppingList.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		UserDetails normalUser = User.withUsername("pawan")
				.password(encoder.encode("password"))
				.roles("NORMAL").build();

		UserDetails adminUser = User.withUsername("admin")
				.password(encoder.encode("password"))
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(normalUser, adminUser);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.authorizeHttpRequests()
//		.requestMatchers("/api/shopping/admin/**").hasRole("ADMIN")
//		.requestMatchers("/api/shopping/normal").hasRole("NORMAL")
//		.requestMatchers("/api/shopping")
//		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin();
		return httpSecurity.build();
	}
}