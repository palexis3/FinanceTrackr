package com.patrickpie12345.server.api.routing

import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductCreate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.service.ProductService
import com.patrickpie12345.storage.UpsertResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin

fun Route.productRouting() {

    val productService by getKoin().inject<ProductService>()

    route("/product") {
        get {
            val result = productService.getAll()
            if (result != null) {
                call.respond(result)
            } else {
                call.respondText("No products found", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val product: Product = productService.get(id) ?: return@get call.respondText(
                "No product with the id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(product)
        }

        post("/update") {
            val productUpdate = call.receive<ProductUpdate>()

            when (productService.update(productUpdate)) {
                is UpsertResult.Ok -> call.respondText("Product updated successfully!", status = HttpStatusCode.Created)
                else -> call.respondText("Product could not be updated..", status = HttpStatusCode.InternalServerError)
            }
        }

        post("/create") {
            val productCreate = call.receive<ProductCreate>()

            when (val productUpsertResult = productService.create(productCreate)) {
                is UpsertResult.Ok -> call.respond(productUpsertResult.result)
                else -> call.respondText("Product could not be created", status = HttpStatusCode.InternalServerError)
            }
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            when (val productDeleteResult = productService.delete(id)) {
                is UpsertResult.Ok -> call.respondText(productDeleteResult.result, status = HttpStatusCode.OK)
                else -> call.respondText("Could not delete product..", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
