//package com.example.sosamo.Config
//
//import org.springframework.context.annotation.Configuration
//import org.springframework.web.socket.config.annotation.EnableWebSocket
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
//
//
//@Configuration
//@EnableWebSocket
//class WebSocketConfig(chatHandler: ChatHandler) : WebSocketConfigurer {
//    private val chatHandler: ChatHandler
//
//    init {
//        this.chatHandler = chatHandler
//    }
//
//    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
//        registry.addHandler(chatHandler, "/ws/chat").setAllowedOrigins("*")
//    }
//}