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

    //  Uncomment for testing since can't get environment variables
//    val flyway: Flyway = Flyway.configure().dataSource(
//        "jdbc:postgresql://localhost:5432/financeTrackr",
//        "postgres",
//        "postgres"
//    ).schemas("public").ignoreMigrationPatterns("*:pending").load()

    try {
        flyway.migrate()
    } catch (e: Exception) {
        flyway.repair()
    }

    // System.getenv("SERVER_PORT").toInt()
    embeddedServer(Netty, port = 8080) {
        configureSerialization()
        configureMonitoring()
        configureDependencyInjection()
        configureRouting()
    }.start(wait = true)
}
