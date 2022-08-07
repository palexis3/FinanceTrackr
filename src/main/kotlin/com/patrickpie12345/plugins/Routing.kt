package com.patrickpie12345.plugins

import com.patrickpie12345.graphql.KtorServer
import com.patrickpie12345.storage.receipts.ReceiptStorage
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.koin.ktor.ext.getKoin

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("graphql") {
            KtorServer().handle(this.call)
        }
    }
}
