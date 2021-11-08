package com.unizar.urlshorter.controllers

import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler

import java.util.*
import java.io.IOException

import org.bson.types.ObjectId

/*
    Class that Controlls WebSocket Behaivour.
    Path to WebSocket initialized in UriShorterApplication.kt
*/
@Component
class WSController : TextWebSocketHandler() {
    
    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.sendMessage(TextMessage("Connection Established!"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        session.sendMessage(TextMessage("Connection Closed, reason: " + status))
    }

    @Throws(InterruptedException::class, IOException::class)
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        //Test response
        session.sendMessage(TextMessage("Response!"))
		println("[DEBUG] Test!");


        //TextMessage should have the shortened URL

        //When a text message is recieved, wait 10 seconds

        //After waiting time is up, get correct URL and send it through the socket
    }
}