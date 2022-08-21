package com.patrickpie12345

import com.patrickpie12345.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 3000, host = "0.0.0.0") {
        configureSerialization()
        configureMonitoring()
        configureDependencyInjection()
        configureRouting()
    }.start(wait = true)
}
