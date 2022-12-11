package com.patrickpie12345.server.api.routing

import com.patrickpie12345.extensions.toFile
import com.patrickpie12345.service.ProductService
import com.patrickpie12345.service.ReceiptService
import com.patrickpie12345.storage.UpsertResult
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import java.io.File

fun Route.imageRouting(
    receiptService: ReceiptService,
    productService: ProductService
) {

    post("/{type?}/{id?}/image") {
        val itemService = when (call.parameters["type"]?.lowercase()) {
            "product" -> productService
            "receipt" -> receiptService
            else -> return@post call.respondText(
                "Invalid type in path URL to upload image. Try either '/product..' or '/receipt..'",
                status = HttpStatusCode.BadRequest
            )
        }
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
                when (itemService.addImage(id, file)) {
                    is UpsertResult.Ok -> call.respond(HttpStatusCode.Created)
                    else -> call.respondText("Internal error: could not save image in database", status = HttpStatusCode.InternalServerError)
                }
            } ?: call.respondText("Internal error: image processed was null", status = HttpStatusCode.InternalServerError)
        } catch (ex: Exception) {
            call.respondText(
                "Exception occurred while processing image",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}
