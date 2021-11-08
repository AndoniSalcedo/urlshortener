package com.unizar.urlshorter.controllers

import com.unizar.urlshorter.repositories.UserRepository
import com.unizar.urlshorter.models.User

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

data class RegisterIn(
    var email: String,
    var name: String,
    var password: String 
)

data class LoginIn(
    var email: String,
    var password: String
)

data class LoginOut(
    var accessToken: String
)

@RestController
@RequestMapping("/auth")
class UserController( val userRepository: UserRepository){
    
    @PostMapping("/register")
    fun register(@RequestBody body: RegisterIn): ResponseEntity<Void>  {

        //TODO: valid data
        var userExist = userRepository.findOneByEmail(body.email)
        //Check if User email already exist
        if(userExist != null){
            return ResponseEntity<Void>(HttpHeaders(), HttpStatus.CONFLIT)
        }
        //Create a new user
        var user = User(body.name,body.email,body.password)
        //Save user
        userRepository.save(user)

        return ResponseEntity<Void>(HttpHeaders(), HttpStatus.CREATED)
    }
    
    @PostMapping("/login")
    fun login(@RequestBody body: LoginIn) : ResponseEntity<LoginOut> {
        
        var user = userRepository.findOneByEmail(body.email)
        //Check if user exist
        if(user == null){
            return ResponseEntity<LoginOut>(null,HttpHeaders(), HttpStatus.NOT_FOUND)
        }
        //Check if password's are the same
        if(!user.comparePassword(body.password)){
            return ResponseEntity<LoginOut>(null,HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }

        var issuer = user.id.toString()
        var jwt = Jwts.builder().setIssuer(issuer).signWith(SignatureAlgorithm.HS512, "secret").compact()
  
        var res = LoginOut(
            accessToken = jwt
        )
        
        return ResponseEntity<LoginOut>(res,HttpHeaders(), HttpStatus.OK)
    }
}