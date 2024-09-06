package de.ostfale.wss.websocketserver.ws

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.net.URI
import java.net.URLDecoder
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionRegistry {

    private val log = LoggerFactory.getLogger(javaClass)
    private val registry: ConcurrentHashMap<CsName, WebSocketSession> = ConcurrentHashMap()

    companion object {
        fun URI.getIdentifier(): String {
            val encodedCSName = this.path.substringAfterLast("/")
            val result = URLDecoder.decode(encodedCSName, "UTF-8")
            require(result.isNotEmpty()) { "CS identifier must not be empty for URI: $this" }
            return result
        }
    }

    fun addSession(session: WebSocketSession) {
        val csUri = session.uri
        if (csUri != null) {
            val csName = csUri.getIdentifier()
            log.info("WS-REGISTRY :: Added session with key: $csName and ws-url: ${session.uri} and ID: ${session.id}")
            registry[CsName(csName)] = session
        } else {
            log.error("WebSocketSession does not have a valid session URI to be registered")
        }
    }

    fun getWebSocketSessionCount(): Long {
        return registry.size.toLong()
    }

    fun retrieveAllSessions(): Set<Map.Entry<CsName, WebSocketSession>> {
        log.debug("Return all WebSocket sessions...")

        return if (registry.isEmpty()) {
            emptySet()
        } else {
            val result = registry.entries.toSet()
            log.debug("WS-REGISTRY :: Returning ${result.size} sessions")
            return result
        }
    }

    fun clearSessions() {
        log.debug("WS-REGISTRY :: Clear all registry sessions")
        registry.clear()
    }
}

@JvmInline
value class CsName(val name: String)
