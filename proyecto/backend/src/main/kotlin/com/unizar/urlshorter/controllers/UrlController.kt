package com.unizar.urlshorter.controllers

import com.unizar.urlshorter.repositories.UrlRepository
import com.unizar.urlshorter.models.Url

import com.unizar.urlshorter.repositories.UserRepository
import com.unizar.urlshorter.models.User


import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

import org.bson.types.ObjectId

data class ShortIn(
    val url: String 
)

data class ShortOut(
    val url: String 
)


@RestController
@RequestMapping("/api")
class UrlController(
    var urlRepository: UrlRepository,
    var userRepository: UserRepository){
    

    @PostMapping("/shorter")
    fun shorter(@RequestBody body: ShortIn): ResponseEntity<ShortOut>  {

        var url = Url(body.url)

        //TODO: TASK 1 CHECK SOME STUFF
        //TODO: Ruben part
        /* Execute async the validation of the url and update field url.isValid to true if the url is correct
                async function{
                    get(url)...
                    if(ok){
                        url.validateUrl()
                        urlRepository.save(url)
                    }  
                }      
           */


        urlRepository.save(url)    

        var res = ShortOut(
            url = url.shorter
        )    

        return ResponseEntity<ShortOut>(res,HttpHeaders(), HttpStatus.CREATED)
    }

    @PostMapping("/user/shorter")
    fun shorter(@RequestBody body: ShortIn, @RequestHeader("accessToken") accessToken: String?): ResponseEntity<Void>  {

        if( accessToken == null){
            return ResponseEntity<Void>(HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        //Extract Id from JWT payload
        var id = Jwts.parser().setSigningKey("secret").parseClaimsJws(accessToken).body
        //Get user
        var user = userRepository.findOneById(ObjectId(id.issuer))
        if(user == null){
            return ResponseEntity<Void>(HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        //Create a URL
        var url = Url(body.url)

        //TODO: TASK 1 CHECK SOME STUFF
        //TODO: Ruben part
        /* Execute async the validation of the url and update field url.isValid to true if the url is correct
                async function{
                    get(url)...
                    if(ok){
                        url.validateUrl()
                        urlRepository.save(url)
                    }  
                }      
           */
        //Add Url to User urls
        user.addUrl(ObjectId(url.id))
        
        //Save url
        urlRepository.save(url)        
        //Save user
        userRepository.save(user)

        return ResponseEntity<Void>(HttpHeaders(), HttpStatus.CREATED)
    }
    
    // TODO: function must be async, when socket recive msg {tiny,id} wait 10s and return the url using sockets
    // TODO: Isaac part
    @GetMapping("/tiny-{id:.*}")
    fun redirect(@PathVariable id: String) : ResponseEntity<ShortOut> {
        
        //Find if the url exist
        var url = urlRepository.findOneByShorter(id)
        if(url == null){
            var res = ShortOut(
                url = "url null"
            )
            return ResponseEntity<ShortOut>(res,HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        // Find if Url is valid
        if(!url.isValid){
            var res = ShortOut(
                url = "url not valid"
            )
            return ResponseEntity<ShortOut>(res,HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        url.addClick()

        urlRepository.save(url)

        var res = ShortOut(
            url = url.url
        )
        
        return ResponseEntity<ShortOut>(res,HttpHeaders(), HttpStatus.CREATED)
    }
}