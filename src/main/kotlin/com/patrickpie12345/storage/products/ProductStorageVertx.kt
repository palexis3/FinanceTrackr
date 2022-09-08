package com.patrickpie12345.storage.products

import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductDBUpdate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.*

class ProductStorageVertx(private val client: SqlClient) : ProductStorage {

    override suspend fun get(id: UUID): Product? =
        fetchRow(
            client = client,
            query = "SELECT * FROM public.products WHERE id = $1",
            args = Tuple.of(id)
        )?.toProduct()

    override suspend fun addProduct(productDBCreate: ProductDBCreate): UpsertResult<Product> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.products (name, price, quantity) VALUES ($1, $2, $3)
                RETURNING *
            """.trimIndent(),
            args = Tuple.of(productDBCreate.name, productDBCreate.price, productDBCreate.quantity)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not insert product: ${productDBCreate.name}")
                else -> UpsertResult.Ok(row.toProduct())
            }
        }

    override suspend fun updateProduct(productDBUpdate: ProductDBUpdate): UpsertResult<Product> =
        fetchRow(
            client = client,
            query = """
                UPDATE public.products SET 
                name = COALESCE($2, name), price = COALESCE($3, price), quantity = COALESCE($4, quantity)
                WHERE id = $1
                RETURNING *
            """.trimIndent(),
            args = Tuple.of(productDBUpdate.id, productDBUpdate.name, productDBUpdate.price, productDBUpdate.quantity)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not update product with id: ${productDBUpdate.id}")
                else -> UpsertResult.Ok(row.toProduct())
            }
        }

    override suspend fun addStoresToProduct(productId: UUID, storeIds: List<UUID>): UpsertResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addImageId(productId: UUID, imageId: UUID): UpsertResult<String> =
        fetchRow(
            client = client,
            query = "UPDATE public.products SET image_id = $2 WHERE id = $1 RETURNING image_id",
            args = Tuple.of(productId, imageId)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not update product id: $productId with image")
                else -> UpsertResult.Ok("Successfully updated product id: $productId with image id: $imageId")
            }
        }
}
