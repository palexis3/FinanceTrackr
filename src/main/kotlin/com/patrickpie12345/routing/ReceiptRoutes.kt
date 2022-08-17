package com.patrickpie12345.routing

import com.patrickpie12345.storage.receipts.ReceiptStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

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
        get("{id?}") {}
        post {}
    }
}
