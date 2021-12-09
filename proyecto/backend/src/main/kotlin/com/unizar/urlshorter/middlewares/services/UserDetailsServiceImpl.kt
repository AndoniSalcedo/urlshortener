/* package com.unizar.urlshorter.middlewares.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.unizar.urlshorter.models.User
import com.fasterxml.jackson.annotation.JsonIgnore;

fun User.toUserDetails() : UserDetails = object : UserDetails {
	
}


class UserDetailsImpl : UserDetails() {
	val serialVersionUID: long = 1L;

	var id: String = ""

	var name: String = ""

	var email: String = ""

	@JsonIgnore
	var password: String = ""

	var authorities: Collection<? : GrantedAuthority>

    companion object{
	    fun build(user: User) UserDetailsImpl {
	    	var authorities = null

            var userDetails = UserDetailsImpl()

            userDetails.id = user.id
	    	userDetails.name = user.name
	    	userDetails.email = user.email
	    	userDetails.password = user.password
	    	userDetails.authorities = authorities

	    	return userDetails
	    }
    }

	override fun getAuthorities(): Collection<? : GrantedAuthority> {
		return authorities;
	}

	fun getId(): String{
		return id;
	}

	fun getEmail(): String {
		return email;
	}

	override fun getPassword(): String {
		return password;
	}

	override fun getUsername(): String {
		return name;
	}

	override fun isAccountNonExpired(): Boolean {
		return true;
	}

	override fun  isAccountNonLocked(): Boolean {
		return true;
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true;
	}

	override fun isEnabled(): Boolean {
		return true;
	}

	override fun equals(o: Object): Boolean {
		return true;
	} 
} */