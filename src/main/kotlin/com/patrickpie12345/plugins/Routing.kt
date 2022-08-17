package com.patrickpie12345.plugins

import com.patrickpie12345.graphql.KtorServer
import com.patrickpie12345.routing.receiptRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("graphql") {
            KtorServer().handle(this.call)
        }

        get("playground") {
            this.call.respondText(buildPlaygroundHtml("graphql", "subscriptions"), ContentType.Text.Html)
        }

        // HTTP APIs
        receiptRouting()
    }
}
private fun buildPlaygroundHtml(graphQLEndpoint: String, subscriptionEndpoint: String) =
    Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
        ?.replace("\${graphQLEndpoint}", graphQLEndpoint)
        ?.replace("\${subscriptionEndpoint", subscriptionEndpoint)
        ?: throw IllegalStateException("graphql-playground.html cannot be found in the classpath")
