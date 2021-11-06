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

import java.net.URI;
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import org.bson.types.ObjectId

data class ShortIn(
    var url: String 
)

data class ShortOut(
    var url: String 
)

data class UrlsOut(
    var urls: ArrayList<Url>?
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
        //TODO: currently is not a must to be async only check if the url is correct and call validate Url 
        /* Execute validation(currently sync) of the url and update field url.isValid to true if the url is correct*/
        fun checkUrl(): Int {
            val client = HttpClient.newBuilder().build();
            val request = HttpRequest.newBuilder()
                    .uri(URI.create(url.url))
                    .build();
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            return response.statusCode()
        }

        var retCode = checkUrl()

        if (retCode == 200){
            url.validateUrl()
            urlRepository.save(url)
        }else{
            url.shorter = "NULO"
        }

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
        //TODO: currently is not a must to be async only check if the url is correct and call validate Url 
        /* Execute validation(currently sync) of the url and update field url.isValid to true if the url is correct*/
        fun checkUrl(): Int {
            val client = HttpClient.newBuilder().build();
            val request = HttpRequest.newBuilder()
                    .uri(URI.create(url.url))
                    .build();
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            return response.statusCode()
        }

        var retCode = checkUrl()

        if (retCode == 200){
            url.validateUrl()
            urlRepository.save(url)
        }

        //Add Url to User urls
        user.addUrl(url)
        
        //Save url
        urlRepository.save(url)        
        //Save user
        userRepository.save(user)

        return ResponseEntity<Void>(HttpHeaders(), HttpStatus.CREATED)
    }
    
    // TODO: function must be async, when socket recive msg {tiny,id} wait 10s and return the url using sockets
    // TODO: Isaac part
    // TODO: make the changes in other file (e.g: SocketController.kt) and let this code as decapreted for the moment
    @GetMapping("/tiny-{shorter:.*}")
    fun redirect(@PathVariable shorter: String) : ResponseEntity<ShortOut> {
        
        //Find if the url exist
        var url = urlRepository.findOneByShorter(shorter)
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
        
        return ResponseEntity<ShortOut>(res, HttpHeaders(), HttpStatus.CREATED)
    }
    
    @GetMapping("/user/urls")
    fun getUrls(@RequestHeader("accessToken") accessToken: String?) : ResponseEntity<UrlsOut> {
        if( accessToken == null){
            var res = UrlsOut(
                urls = null
            )
            return ResponseEntity<UrlsOut>(res, HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        //Extract Id from JWT payload
        var id = Jwts.parser().setSigningKey("secret").parseClaimsJws(accessToken).body
        //Get user
        var user = userRepository.findOneById(ObjectId(id.issuer))

        if(user == null){
            var res = UrlsOut(
                urls = null
            )
            return ResponseEntity<UrlsOut>(res ,HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }

        var res = UrlsOut(
            urls = user.urls
        )

        return ResponseEntity<UrlsOut>(res,HttpHeaders(), HttpStatus.CREATED)

    }
}