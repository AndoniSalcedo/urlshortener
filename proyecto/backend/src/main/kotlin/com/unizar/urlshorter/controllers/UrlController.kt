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

    fun checkUrl(url: Url): Void {
        val client = HttpClient.newBuilder().build();
        val req = HttpRequest.newBuilder()
                .uri(URI.create(url.url))
                .build();
        val res = client.send(req, HttpResponse.BodyHandlers.ofString())
        if (res.statusCode() == 200){
            url.validateUrl()
            urlRepository.save(url)
        }
    }
    

    @PostMapping("/shorter")
    fun shorter(@RequestBody body: ShortIn): ResponseEntity<ShortOut>  {
        //Create url
        var url = Url(body.url)
        //Check if url us correct
        checkUrl(url)
        //Save url
        urlRepository.save(url)
        var res = ShortOut(
                url = url.shorter
        )
        return ResponseEntity<ShortOut>(res,HttpHeaders(), HttpStatus.CREATED)
    }

    @PostMapping("/user/shorter")
    fun shorter(@RequestBody body: ShortIn, @RequestHeader("accessToken") accessToken: String?): ResponseEntity<ShortOut>  {

        if( accessToken == null){
            return ResponseEntity<ShortOut>(null,HttpHeaders(), HttpStatus.BAD_REQUEST)
        }
        //Extract Id from JWT payload
        var id = Jwts.parser().setSigningKey("secret").parseClaimsJws(accessToken).body
        //Get user
        var user = userRepository.findOneById(ObjectId(id.issuer))
        if(user == null){
            return ResponseEntity<ShortOut>(null,HttpHeaders(), HttpStatus.NOT_FOUND)
        }
        //Create a URL
        var url = Url(body.url)
        //Check if url is correct
        checkUrl(url)
        //Add Url to User urls
        user.addUrl(url)
        //Save url
        urlRepository.save(url)        
        //Save user
        userRepository.save(user)

        var res = ShortOut(
                url = url.shorter
        )

        return ResponseEntity<ShortOut>(res,HttpHeaders(), HttpStatus.CREATED)
    }

    @GetMapping("/user/urls")
    fun getUrls(@RequestHeader("accessToken") accessToken: String?) : ResponseEntity<UrlsOut> {
        //TODO: to solve url dont return url.click's update
        //Check if header exist
        if( accessToken == null){
            return ResponseEntity<UrlsOut>(null, HttpHeaders(), HttpStatus.BAD_REQUEST)
        }
        //Extract Id from JWT payload
        //TODO: global secret
        var id = Jwts.parser().setSigningKey("secret").parseClaimsJws(accessToken).body
        //Get user
        var user = userRepository.findOneById(ObjectId(id.issuer))
        //Check if user exist
        if(user == null){
            return ResponseEntity<UrlsOut>(null ,HttpHeaders(), HttpStatus.NOT_FOUND)
        }
        //Create response
        var res = UrlsOut(
            urls = user.urls
        )

        return ResponseEntity<UrlsOut>(res,HttpHeaders(), HttpStatus.OK)
    }
    
    // Depecrated func
    @GetMapping("/tiny-{shorter:.*}")
    fun redirect(@PathVariable shorter: String) : ResponseEntity<ShortOut> {
        
        //Find if the url exist
        var url = urlRepository.findOneByShorter(shorter)
        if(url == null){
            return ResponseEntity<ShortOut>(null,HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        // Find if Url is valid
        if(!url.isValid){
            return ResponseEntity<ShortOut>(null,HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }

        fun addClick(): Void {
            url.addClick()
            urlRepository.save(url)
        }

        var res = ShortOut(
            url = url.url
        )
        
        return ResponseEntity<ShortOut>(res, HttpHeaders(), HttpStatus.CREATED)
    }
}