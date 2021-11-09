package com.unizar.urlshorter.controllers

import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler

import java.util.*
import java.io.IOException
import java.lang.Thread

import org.bson.types.ObjectId

/*
    Class that Controlls WebSocket Behaivour.
    Path to WebSocket initialized in UriShorterApplication.kt
*/
@Component
class WSController : TextWebSocketHandler() {
    
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
		println("Incoming Message: '" + message.payload + "', waiting 10 seconds...");

        var shortURL = message.payload

        //Retrieve long URL from short URL

        var longURL = "http://www.google.com"

        Thread.sleep(10000)

        println("Sending Response...");
        session.sendMessage(TextMessage(longURL))
    }
}