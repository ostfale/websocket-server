package de.ostfale.wss.websocketserver.ws

import org.slf4j.LoggerFactory
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

class SessionHandshakeInterceptor : HttpSessionHandshakeInterceptor() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: MutableMap<String, Any>): Boolean {
        log.info("Interceptor :: beforeHandshake")
        return super.beforeHandshake(request, response, wsHandler, attributes)
    }
}
