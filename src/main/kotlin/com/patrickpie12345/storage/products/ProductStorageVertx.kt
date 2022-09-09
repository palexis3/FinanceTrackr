package com.patrickpie12345.storage.products

import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.batch
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

    override suspend fun saveProduct(productDBCreate: ProductDBCreate): UpsertResult<Product> =
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

    override suspend fun updateProduct(productUpdate: ProductUpdate): UpsertResult<Product> =
        fetchRow(
            client = client,
            query = """
                UPDATE public.products SET 
                name = COALESCE($2, name), price = COALESCE($3, price), quantity = COALESCE($4, quantity)
                WHERE id = $1
                RETURNING *
            """.trimIndent(),
            args = Tuple.of(productUpdate.id, productUpdate.name, productUpdate.price, productUpdate.quantity)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not update product with id: ${productUpdate.id}")
                else -> UpsertResult.Ok(row.toProduct())
            }
        }

    override suspend fun addProductToStores(productAndStoreTuples: List<Tuple>): UpsertResult<String> =
        batch(
            client = client,
            query = """
                INSERT INTO public.products_stores (product_id, store_id, updated_at, expired_at)
                VALUES ($1, $2, $3, $4) RETURNING *
            """.trimIndent(),
            args = productAndStoreTuples
        ).let { res ->
            return if (res.rowCount() > 0) {
                UpsertResult.Ok("Successfully inserted in batched entries for addStoresToProduct")
            } else {
                UpsertResult.NotOk("Failed to insert in batched entries for addStoresToProduct")
            }
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
