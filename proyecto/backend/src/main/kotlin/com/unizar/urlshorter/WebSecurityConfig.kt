package com.unizar.urlshorter

import com.unizar.urlshorter.middlewares.JWTAuthorizationFilter

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
	@Autowired
	lateinit var jsonAuthorizationFilter: JWTAuthorizationFilter

	override fun configure(http: HttpSecurity) {
		with(http) {
			csrf().disable()

			cors().and()

            headers().frameOptions().sameOrigin()

			addFilterAfter(jsonAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
			
			authorizeRequests()
			.antMatchers(HttpMethod.POST, "/auth/*").permitAll()
            .antMatchers(HttpMethod.POST, "/api/shorter").permitAll()
            .antMatchers(HttpMethod.POST, "/api/qr").permitAll()
			.antMatchers(HttpMethod.POST, "/ad/obtain").permitAll()
            .antMatchers("/wstimer").permitAll()
			.anyRequest().authenticated()
		}
	}
}