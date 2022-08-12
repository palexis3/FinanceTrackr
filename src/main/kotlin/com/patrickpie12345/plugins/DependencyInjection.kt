package com.patrickpie12345.plugins

import com.patrickpie12345.DI
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger
fun Application.configureDependencyInjection() {
    install(Koin) {
        SLF4JLogger()
        modules(DI.storageModule())
    }
}
