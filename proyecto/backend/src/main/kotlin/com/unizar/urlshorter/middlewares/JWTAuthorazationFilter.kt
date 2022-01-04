package com.unizar.urlshorter.middlewares

import java.io.IOException;
import java.util.ArrayList;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.core.GrantedAuthority

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts

import org.springframework.stereotype.Component

@Component
class JWTAuthorizationFilter : OncePerRequestFilter() {

	val HEADER : String = "Authorization";
	val PREFIX : String = "Bearer ";
	val SECRET : String = "secret";

	override fun doFilterInternal(req: HttpServletRequest, res :  HttpServletResponse, chain : FilterChain ){
		if (existeJWTToken(req)) {
			validateToken(req)?.let{
				
				var auth = UsernamePasswordAuthenticationToken(it.issuer, null,ArrayList<GrantedAuthority>());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}?:run{
				SecurityContextHolder.clearContext();	
			}
		} else {
			SecurityContextHolder.clearContext();	
		}
		chain.doFilter(req, res);
	}	

	fun validateToken(req: HttpServletRequest): Claims? {
		var jwtToken = req.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken).getBody();
	}


	fun existeJWTToken(req: HttpServletRequest): Boolean {
		var authenticationHeader = req.getHeader(HEADER);
		return !(authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
	}

}