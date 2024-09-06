package de.ostfale.wss.websocketserver.ws

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator


@Configuration
@EnableWebSocket
@EnableConfigurationProperties(WebSocketConfigProperties::class)
class WebSocketConfig(
    val webSocketConfigProperties: WebSocketConfigProperties,

    ) : WebSocketConfigurer {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        logger.info("WS :: Register WebSocket handler")
        registry.addHandler(ExceptionWebSocketHandlerDecorator(webSocketHandler()), webSocketConfigProperties.endpointV2)
            .addInterceptors(sessionInterceptor())
            .setAllowedOrigins(webSocketConfigProperties.allowedOrigins)
    }

    @Bean
    fun sessionInterceptor(): SessionHandshakeInterceptor {
        logger.info("WS :: Init WebSocket interceptor")
        return SessionHandshakeInterceptor()
    }

    @Bean
    fun webSocketHandler(): WebSocketHandler {
        logger.info("WS :: Init WebSocket handler")
        return WebSocketHandler(WebSocketSessionRegistry())
    }
}

@ConfigurationProperties(prefix = "ocpp.ws")
data class WebSocketConfigProperties(
    val endpointV2: String,
    val allowedOrigins: String
)
