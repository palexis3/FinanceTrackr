package com.patrickpie12345

import com.patrickpie12345.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway

fun main() {
    val flyway: Flyway = Flyway.configure().dataSource(
        Env.jdbcDatabaseUrl,
        Env.databaseUserName,
        Env.databasePassword
    ).schemas("public").ignoreMigrationPatterns("*:pending").load()

    try {
        flyway.migrate()
    } catch (e: Exception) {
        flyway.repair()
    }

    embeddedServer(Netty, port = Env.serverPort) {
        configureSerialization()
        configureMonitoring()
        configureDependencyInjection()
        configureRouting()
    }.start(wait = true)
}
