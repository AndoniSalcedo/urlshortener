package com.unizar.urlshorter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler

import com.unizar.urlshorter.controllers.WSController

// Sets-up WebSocket Behaivour "WSController" at path /wstimer
@Configuration @EnableWebSocket
class WSConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WSController(), "/wstimer").withSockJS()
		println("WSConfig Registered!");
    }
}

@SpringBootApplication
class UrlshorterApplication

fun main(args: Array<String>) {
	runApplication<UrlshorterApplication>(*args)
}
