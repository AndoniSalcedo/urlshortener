package com.unizar.urlshorter.controllers

import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler

import java.util.*
import java.io.IOException
import java.lang.Thread

import org.bson.types.ObjectId

import com.unizar.urlshorter.repositories.UrlRepository
import com.unizar.urlshorter.repositories.UserRepository
import com.unizar.urlshorter.models.Url
import com.unizar.urlshorter.models.User
import com.unizar.urlshorter.controllers.UrlController
import com.unizar.urlshorter.UrlshorterApplication

/*
    Class that Controlls WebSocket Behaivour.
    Path to WebSocket initialized in UriShorterApplication.kt
*/
@Component
class WSController(var urlRepository: UrlRepository, var userRepository: UserRepository) : TextWebSocketHandler() {
    
    //@Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        //session.sendMessage(TextMessage("Connection Established!"))
        println("WebSocket connection established");
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        //session.sendMessage(TextMessage("Connection Closed, reason: " + status))
        println("WebSocket connection closed");
    }

    //@Throws(InterruptedException::class, IOException::class)
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        //Test response

        var shortURL = message.payload
        //TODO: No se deberia rellenar la URL con localhost
        shortURL = "http://localhost:3000/api/" + shortURL

        //Find if the url exist
        try {
            var url = urlRepository.findOneByShorter(shortURL)
            if(url == null) {
                println("URL Error: '" + message.payload + "' Retrieved URL is null")
                session.sendMessage(TextMessage("https://en.wikipedia.org/wiki/HTTP_404"))
                return
            }

            url.addClick()

            var longURL = url.url

            println("Incoming Request: '" + message.payload + ". URL found: " + url.url + ". Waiting 10 seconds...");
            Thread.sleep(10000) //Wait 10 seconds
        
            println("Sending Response...");
            session.sendMessage(TextMessage(longURL))
        } catch(e: Exception) {
            println("Invalid Request: Short URL '" + message.payload + "' doesn't exist")
            session.sendMessage(TextMessage("https://en.wikipedia.org/wiki/HTTP_404"))
        }
    }
}