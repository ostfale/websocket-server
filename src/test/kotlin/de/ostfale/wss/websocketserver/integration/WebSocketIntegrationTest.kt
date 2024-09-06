package de.ostfale.wss.websocketserver.integration

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestClient
import org.testcontainers.Testcontainers.exposeHostPorts
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
class WebSocketIntegrationTest {

    private val restClient: RestClient = RestClient.builder().baseUrl(prepareWebProtocolBasePath()).build()

    companion object {
        private const val IMAGE_NAME = "ws-client-test:latest"
        private const val IMAGE_PORT = 21088
        private const val URI_PROTOCOL = "http://"
        private const val URI_PATH = "/tc/api/v1/action"

        @Container
        @JvmStatic
        val wsContainer = GenericContainer<Nothing>(IMAGE_NAME).apply {
            withExposedPorts(IMAGE_PORT)
            withAccessToHost(true)
        }


        @JvmStatic
        @BeforeAll
        fun setUp(): Unit {
            exposeHostPorts(29080)
        }
    }

    @Test
    fun containerIsRunningTest() {
        assertTrue(wsContainer.isRunning)
    }

    @Test
    fun sendingPingRetrievingPongTest() {
        val response = restClient.get()
            .uri(URI_PATH + "/ping")
            .retrieve()
            .body(String::class.java)

        assertNotNull(response)
        assertEquals("Pong", response)
    }

    @Test
    fun sendConnectRequestTest() {
        val response = restClient.get()
            .uri(URI_PATH + "/connect")
            .retrieve()
            .body(String::class.java)
    }

    @Test
    fun testWebClientConnectionToContainer() {
        val response = restClient.get()
            .uri(URI_PATH + "/sessionid")
            .retrieve()
            .body(String::class.java)

        assertNotNull(response)
    }

    private fun prepareWebProtocolBasePath(): String {
        val port = wsContainer.firstMappedPort
        val host = wsContainer.host

        val basePath = "${URI_PROTOCOL}$host:$port"
        return basePath
    }
}
