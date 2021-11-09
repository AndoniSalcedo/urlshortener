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
import java.awt.image.BufferedImage
import com.google.zxing.client.j2se.MatrixToImageWriter
import java.io.ByteArrayOutputStream

import javax.imageio.ImageIO;

import org.bson.types.ObjectId


data class ShortIn(
    var url: String 
)

data class ShortOut(
    var url: String 
)

data class QrOut(
    var qr: String
)

data class UrlsOut(
    var urls: ArrayList<Url>?
)

@RestController
@RequestMapping("/api")
class UrlController(
    var urlRepository: UrlRepository,
    var userRepository: UserRepository){
    
    
    fun checkUrl(url: Url) {
        try {
            val client = HttpClient.newBuilder().build();
            val req = HttpRequest.newBuilder()
                .uri(URI.create(url.url))
                .build();
            client.send(req, HttpResponse.BodyHandlers.ofString())
            //If res.status != 2** throw a exception and dont save the url salid
            url.validateUrl()
            urlRepository.save(url)   
        } catch(ex: Exception) {

        }
    }
    

    @PostMapping("/shorter")
    fun shorter(@RequestBody body: ShortIn): ResponseEntity<ShortOut> {
        //Create url
        var url = Url(body.url)
        //Check if url us correct
        checkUrl(url)
        //Save url
        var res = ShortOut(
            url = url.shorter
        )
        return ResponseEntity<ShortOut>(res, HttpHeaders(), HttpStatus.CREATED)
    }

    

    @PostMapping("/user/shorter")
    fun userShorter(@RequestBody body: ShortIn, @RequestHeader("accessToken") accessToken: String?): ResponseEntity<ShortOut>  {
        //Check if header exist
        if( accessToken == null){
            return ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.BAD_REQUEST)
        }
        //Extract Id from JWT payload
        //TODO: global secret
        var id = Jwts.parser().setSigningKey("secret").parseClaimsJws(accessToken).body
        //Get user
        var user = userRepository.findOneById(ObjectId(id.issuer))
        //Check if user exist
        if(user == null){
            return ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.NOT_FOUND)
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

        return ResponseEntity<ShortOut>(res, HttpHeaders(), HttpStatus.CREATED)
    }
    //TODO: how to return qr
    @GetMapping("/qr/{id}")
    fun qr(@PathVariable id: String): ResponseEntity<QrOut>  {
        //Find url by Id
        var url = urlRepository.findOneById(id)
        val bos = ByteArrayOutputStream()
        // Encodes BitMatrix as a PNG type Image, then its coded as a Base64 type array.
        MatrixToImageWriter.writeToStream(url?.qr, "PNG", bos)
        val image = Base64.getEncoder().encodeToString(bos.toByteArray())
        // In the front, this could be displayed as a base64 coded png , like "<img src={`data:image/png;base64,${image}`} />"
        var res = QrOut(
            qr = image
        )
        return ResponseEntity<QrOut>(res, HttpHeaders(), HttpStatus.CREATED)
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

        return ResponseEntity<UrlsOut>(res, HttpHeaders(), HttpStatus.OK)
    }
    
    // Depecrated func
    @Deprecated(message = "Past Test Functionality")
    @GetMapping("/tiny-{shorter:.*}")
    fun redirect(@PathVariable shorter: String) : ResponseEntity<ShortOut> {
        
        //Find if the url exist
        var url = urlRepository.findOneByShorter(shorter)
        if(url == null){
            return ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }
        // Find if Url is valid
        if(!url.isValid){
            return ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.UNAUTHORIZED)
        }

        fun addClick() {
            url.addClick()
            urlRepository.save(url)
        }

        addClick()

        var res = ShortOut(
            url = url.url
        )
        
        return ResponseEntity<ShortOut>(res, HttpHeaders(), HttpStatus.CREATED)
    }
}