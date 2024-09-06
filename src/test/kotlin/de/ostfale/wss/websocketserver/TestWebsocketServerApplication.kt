package de.ostfale.wss.websocketserver

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<WebsocketServerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
