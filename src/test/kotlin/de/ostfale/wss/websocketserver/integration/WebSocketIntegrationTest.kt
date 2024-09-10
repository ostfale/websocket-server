package de.ostfale.wss.websocketserver.integration

import org.awaitility.Awaitility.await
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestClient
import org.testcontainers.Testcontainers.exposeHostPorts
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeUnit.SECONDS

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WebSocketIntegrationTest {

    private val restClient: RestClient = RestClient.builder().baseUrl(prepareWebProtocolBasePath()).build()

    companion object {
        private const val IMAGE_NAME = "ws-client-test:latest"
        private const val IMAGE_PORT = 21088
        private const val LOCAL_PORT = 29080
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
        fun setUp() {
            exposeHostPorts(LOCAL_PORT)
        }
    }

    @Test
    @Order(1)
    fun containerIsRunningTest() {
        assertTrue(wsContainer.isRunning)
    }

    @Test
    @Order(2)
    fun sendConnectRequestTest() {
        restClient.get()
            .uri("$URI_PATH/connect")
            .retrieve()
    }

    @Test
    @Order(3)
    fun sendingPingRetrievingPongTest() {
        val response = restClient.get()
            .uri(URI_PATH + "/ping")
            .retrieve()
            .body(String::class.java)

        assertNotNull(response)
        assertEquals("Pong", response)
    }

    @Test
    @Order(4)
    fun testWebClientConnectionToContainer() {
        await().atMost(1, SECONDS).untilAsserted {
            val response = restClient.get()
                .uri("$URI_PATH/sessionid")
                .retrieve()
                .body(String::class.java)
            assertNotNull(response)
        }
    }

    private fun prepareWebProtocolBasePath(): String {
        val port = wsContainer.firstMappedPort
        val host = wsContainer.host

        val basePath = "${URI_PROTOCOL}$host:$port"
        return basePath
    }
}
