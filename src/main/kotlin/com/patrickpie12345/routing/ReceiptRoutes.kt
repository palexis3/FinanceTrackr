package com.patrickpie12345.routing

import com.patrickpie12345.graphql.models.ReceiptCreate
import com.patrickpie12345.storage.receipts.ReceiptStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent
import java.util.*

fun Route.receiptRouting() {

    val receiptStorage by KoinJavaComponent.getKoin().inject<ReceiptStorage>()

    route("/receipt") {
        get {
            val receiptWrapper = receiptStorage.getAll()
            if (receiptWrapper != null && receiptWrapper.items.isNotEmpty()) {
                call.respond(receiptWrapper)
            } else {
                call.respondText("No receipts found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val receipt = receiptStorage.get(UUID.fromString(id)) ?: return@get call.respondText(
                "No receipt with the id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(receipt)
        }
        post {
            val receipt = call.receive<ReceiptCreate>()
            receiptStorage.create(receipt)
            call.application.environment.log.debug("$receipt")
            call.respondText("Receipt stored correctly", status = HttpStatusCode.Created)
        }
    }
}
