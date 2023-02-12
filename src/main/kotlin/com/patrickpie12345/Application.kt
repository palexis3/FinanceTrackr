package com.patrickpie12345

import com.patrickpie12345.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway

fun main() {
    val flyway: Flyway = Flyway.configure().dataSource(
        System.getenv("JDBC_DATABASE_URL"),
        System.getenv("DATABASE_USERNAME"),
        System.getenv("DATABASE_PASSWORD")
    ).schemas("public").ignoreMigrationPatterns("*:pending").load()

    // Comment out for testing
//    val flyway: Flyway = Flyway.configure().dataSource(
//        "jdbc:postgresql://localhost:5432/financeTrackr?user=postgres&password=postgres",
//       "postgres",
//        "postgres"
//    ).schemas("public").ignoreMigrationPatterns("*:pending").load()

    try {
        flyway.migrate()
    } catch (e: Exception) {
        flyway.repair()
    }

    val SERVER_PORT = System.getenv("SERVER_PORT").toInt()
    // Comment out for testing
//    val SERVER_PORT = 8080

    embeddedServer(Netty, port = SERVER_PORT) {
        configureSerialization()
        configureMonitoring()
        configureDependencyInjection()
        configureRouting()
    }.start(wait = true)
}
