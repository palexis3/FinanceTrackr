package com.patrickpie12345.plugins

import com.patrickpie12345.routing.receiptRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // HTTP APIs
        receiptRouting()

        /**
         *  TODO: Patrick get back to implementing GraphQL Routing
         *
         *  post("graphql") {
         *      post("graphql") {
         *  }
         *
         *  get("playground") {
         *     this.call.respondText(buildPlaygroundHtml("graphql", "subscriptions"), ContentType.Text.Html)
         *  }
         */
    }
}
private fun buildPlaygroundHtml(graphQLEndpoint: String, subscriptionEndpoint: String) =
    Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
        ?.replace("\${graphQLEndpoint}", graphQLEndpoint)
        ?.replace("\${subscriptionEndpoint", subscriptionEndpoint)
        ?: throw IllegalStateException("graphql-playground.html cannot be found in the classpath")
