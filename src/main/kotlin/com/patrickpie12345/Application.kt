package com.patrickpie12345

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.patrickpie12345.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureMonitoring()
        configureRouting()
        configureDependencyInjection()
    }.start(wait = true)



}
