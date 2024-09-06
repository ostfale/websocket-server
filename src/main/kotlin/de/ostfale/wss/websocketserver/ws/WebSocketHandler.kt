package de.ostfale.wss.websocketserver.ws

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebSocketHandler(
    private val webSocketSessionRegistry: WebSocketSessionRegistry
) : TextWebSocketHandler(), SubProtocolCapable {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getSubProtocols(): MutableList<String> {
        log.info("WS :: getSubProtocols")
        return mutableListOf("ocpp2.0.1")
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info(" WS :: afterConnectionEstablished")
        webSocketSessionRegistry.addSession(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        log.info("WS :: handleMessage")
        super.handleMessage(session, message)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        log.info("WS :: handleTransportError")
        super.handleTransportError(session, exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        log.info("WS :: afterConnectionClosed")
        super.afterConnectionClosed(session, status)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        log.info("WS :: handleTextMessage")
        super.handleTextMessage(session, message)
    }

    override fun handlePongMessage(session: WebSocketSession, message: PongMessage) {
        log.info("WS :: handlePongMessage")
        super.handlePongMessage(session, message)
    }
}
