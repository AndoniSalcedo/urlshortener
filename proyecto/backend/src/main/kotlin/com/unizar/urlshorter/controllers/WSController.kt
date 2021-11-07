package com.unizar.urlshorter.controllers

import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler

import java.util.*

import org.bson.types.ObjectId

/*
    Class that Controlls WebSocket Behaivour.
    Path to WebSocket initialized in UriShorterApplication.kt
*/
class WSController : TextWebSocketHandler() {
    
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        //Nothing should be done here
    }

    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        //Test response
        session.sendMessage(TextMessage("Response!"))
		println("[DEBUG] Test!");


        //TextMessage should have the shortened URL

        //When a text message is recieved, wait 10 seconds

        //After waiting time is up, get correct URL and send it through the socket
    }
}