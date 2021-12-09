package com.unizar.urlshorter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler

import com.unizar.urlshorter.controllers.WSController

// Sets-up WebSocket Behaivour "WSController" at path /wstimer
@Configuration @EnableWebSocket
class WSConfig : WebSocketConfigurer {
    @Autowired
    lateinit var socketHandler: WSController
    
    //Agradecimientos a https://jeffgardner.tech/spring-boot-web-sockets/ por ser el
    //único sitio donde dicen como usar WebSockets en Springboot y que funcione de verdad

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(socketHandler, "/wstimer").setAllowedOrigins("*")
		println("WSConfig Registered!");
    }
}

@SpringBootApplication
class UrlshorterApplication
fun main(args: Array<String>) {
	runApplication<UrlshorterApplication>(*args)
}
