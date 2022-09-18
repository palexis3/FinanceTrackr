package com.patrickpie12345.plugins

import com.patrickpie12345.server.api.routing.imageRouting
import com.patrickpie12345.server.api.routing.productRouting
import com.patrickpie12345.server.api.routing.receiptRouting
import com.patrickpie12345.service.ProductService
import com.patrickpie12345.service.ReceiptService
import com.patrickpie12345.service.analytics.ProductsAnalyticsService
import com.patrickpie12345.service.analytics.ReceiptAnalyticsService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin

fun Application.configureRouting() {
    install(Routing)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // REST APIs
        val productService by getKoin().inject<ProductService>()
        val productsAnalyticsService by getKoin().inject<ProductsAnalyticsService>()
        productRouting(productService, productsAnalyticsService)

        val receiptService by getKoin().inject<ReceiptService>()
        val receiptAnalyticsService by getKoin().inject<ReceiptAnalyticsService>()
        receiptRouting(receiptService, receiptAnalyticsService)

        imageRouting(receiptService, productService)

        /**
         *  TODO: Patrick get back to implementing GraphQL Routing
         *
         *  post("graphql") {
         *      KtorServer().handle(this.call)
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
