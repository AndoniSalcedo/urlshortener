package com.unizar.urlshorter.controllers

import com.unizar.urlshorter.repositories.UserRepository
import com.unizar.urlshorter.models.User

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import org.springframework.scheduling.concurrent.*
import org.springframework.scheduling.annotation.*
import org.springframework.context.annotation.*
import java.util.concurrent.*

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

data class RegisterIn(
    var email: String,
    var name: String,
    var password: String 
)

data class LoginIn(
    var name: String,
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

        var userExist = userRepository.findOneByEmail(body.email)
        //Check if User email already exist
        if(userExist != null){
            return  ResponseEntity<Void>(HttpHeaders(), HttpStatus.CONFLICT)
        }
        //Create a new user
        var user = User(body.name,body.email,body.password)
        //Save user
        userRepository.save(user)

        return ResponseEntity<Void>(HttpHeaders(), HttpStatus.CREATED)
    }
}