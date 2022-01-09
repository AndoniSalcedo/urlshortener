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

import javax.imageio.ImageIO;

import org.bson.types.ObjectId

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.scheduling.concurrent.*
import org.springframework.scheduling.annotation.*
import org.springframework.context.annotation.*
import java.util.concurrent.*
import org.springframework.beans.factory.annotation.Autowired;

data class ShortIn(
    var url: String,
    var qr: Boolean
)

data class ShortOut(
    var url: String 
)

data class QrIn(
    var url: String
)

data class QrOut(
    var qr: String?
)

data class UrlsOut(
    var urls: ArrayList<Url>?
)



@RestController
@RequestMapping("/api")
class UrlController(
    var urlRepository: UrlRepository,
    var userRepository: UserRepository){

    // Async
    @Async("taskExecutor")
    fun checkUrl(url: Url) {
 
        val client = HttpClient.newBuilder().build();
        val req = HttpRequest.newBuilder()
            .uri(URI.create(url.url))
            .build();
        client.send(req, HttpResponse.BodyHandlers.ofString())
        //If res.status != 2** throw a exception and dont save the url salid
        url.validateUrl()
        urlRepository.save(url)   

    }

    @Async("requestExecutor")
    @PostMapping("/shorter")
    fun shorter(@RequestBody body: ShortIn): CompletableFuture<ResponseEntity<ShortOut>> {

        val result = urlRepository.findOneByUrl(body.url)?.let{
            it.shorter
        } ?: run{
            //Create url
            var url = Url(body.url)
            // Check if it's need to create a QR
            if (body.qr){
                url.addQR()
            }
            //Check if url us correct
            checkUrl(url) // a encolar
            //Save url
            url.shorter

        }
        val shortOut = ShortOut(
            url = result
        )
       return CompletableFuture.completedFuture(ResponseEntity<ShortOut>(shortOut, HttpHeaders(), HttpStatus. CREATED))
    }

    //@Async("requestExecutor")
    @PostMapping("/user/shorter")
    fun userShorter(@RequestBody body: ShortIn): ResponseEntity<ShortOut>  {
        var id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()

        var user = userRepository.findOneById(ObjectId(id))
        //Check if user exist
        if(user == null){
            return ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.NOT_FOUND)
        }
        
        val result = urlRepository.findOneByUrl(body.url)?.let{
            //URL currently exist
            //Add Url to User urls
            user.addUrl(it)
            //Save user
            userRepository.save(user)
            it.shorter
           
        }?: run{
            //URL dont exist
            //Create url
            var url = Url(body.url)
            // Check if it's need to create a QR
            if (body.qr){
                url.addQR()
            }
            //Check if url us correct
            checkUrl(url)
            //Add Url to User urls
            user.addUrl(url)
            //Save user
            userRepository.save(user)
            url.shorter
 
        }

        val shortOut = ShortOut(
            url = result
        ) 
        return ResponseEntity<ShortOut>(shortOut, HttpHeaders(), HttpStatus.CREATED)
    }

    @Async("requestExecutor")
    @PostMapping("/qr")
    fun qr(@RequestBody body: QrIn): CompletableFuture<ResponseEntity<QrOut>>  {
        //Find url by Id
        var url = urlRepository.findOneByUrl(body.url)

        var res = QrOut(
            qr = url?.qr
        )
        return CompletableFuture.completedFuture(ResponseEntity<QrOut>(res, HttpHeaders(), HttpStatus.CREATED))
    }

    //@Async("requestExecutor")
    @GetMapping("/user/urls")
    fun getUrls() : CompletableFuture<ResponseEntity<UrlsOut>> {

        var id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
        //Get user
        var user = userRepository.findOneById(ObjectId(id))
        //Check if user exist
        if(user == null){
            return CompletableFuture.completedFuture(ResponseEntity<UrlsOut>(null ,HttpHeaders(), HttpStatus.NOT_FOUND))
        }
        //Create response
        var res = UrlsOut(
            urls = user.urls
        )

        return CompletableFuture.completedFuture(ResponseEntity<UrlsOut>(res, HttpHeaders(), HttpStatus.OK))
    }
    
    // Depecrated func
    @Async("requestExecutor")
    @Deprecated(message = "Past Test Functionality")
    @GetMapping("/tiny-{shorter:.*}")
    fun redirect(@PathVariable shorter: String) : CompletableFuture<ResponseEntity<ShortOut>> {
        
        //Find if the url exist
        var url = urlRepository.findOneByShorter(shorter)
        if(url == null){
            return CompletableFuture.completedFuture(ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.UNAUTHORIZED))
        }
        // Find if Url is valid
        if(!url.isValid){
            return CompletableFuture.completedFuture(ResponseEntity<ShortOut>(null, HttpHeaders(), HttpStatus.UNAUTHORIZED))
        }

        var res = ShortOut(
            url = url.url
        )
        
        return CompletableFuture.completedFuture(ResponseEntity<ShortOut>(res, HttpHeaders(), HttpStatus.CREATED))
    }
}