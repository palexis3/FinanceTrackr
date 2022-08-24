package com.patrickpie12345.server.api.routing

import com.patrickpie12345.extensions.toFile
import com.patrickpie12345.models.Receipt
import com.patrickpie12345.models.ReceiptCreate
import com.patrickpie12345.service.ReceiptService
import com.patrickpie12345.storage.UpsertResult
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin
import java.io.File

fun Route.receiptRouting() {

    val receiptService by getKoin().inject<ReceiptService>()

    route("/receipt") {
        get {
            val result = receiptService.getAll()
            if (result != null) {
                call.respond(result)
            } else {
                call.respondText("No receipts found", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val receipt: Receipt = receiptService.get(id) ?: return@get call.respondText(
                "No receipt with the id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(receipt)
        }

        post {
            val receiptCreate = call.receive<ReceiptCreate>()
            call.application.environment.log.debug("$receiptCreate")

            when (receiptService.create(receiptCreate)) {
                is UpsertResult.Ok -> {
                    call.respondText("Receipt stored correctly", status = HttpStatusCode.Created)
                }
                else -> {
                    call.respondText("Receipt could not be created and stored.", status = HttpStatusCode.InternalServerError)
                }
            }
        }

        post("{id?}/image") {
            val id = call.parameters["id"] ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            try {
                var image: File? = null

                call.receiveMultipart().forEachPart { partData ->
                    when (partData) {
                        is PartData.FileItem -> image = partData.toFile()
                        else -> Unit
                    }
                    partData.dispose
                }

                image?.let { file ->
                    when (receiptService.addImage(id, file)) {
                        is UpsertResult.Ok -> call.respond(HttpStatusCode.Created)
                        else -> call.respond(HttpStatusCode.InternalServerError)
                    }
                } ?: call.respond(HttpStatusCode.InternalServerError)
            } catch (ex: Exception) {
                call.respondText(
                    "Exception occurred while processing image",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}
