package com.patrickpie12345.storage.products

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import com.patrickpie12345.storage.VertxStorageExtension.fetchRowSet
import com.patrickpie12345.storage.images.ItemImageStorage
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.*

class ProductStorageVertx(private val client: SqlClient) : ProductStorage, ItemImageStorage {

    override suspend fun get(id: UUID): Product? =
        fetchRow(
            client = client,
            query = "SELECT * FROM public.products WHERE id = $1",
            args = Tuple.of(id)
        )?.toProduct()

    override suspend fun getAll(): Page<Product>? =
        fetchRowSet(
            client = client,
            query = "SELECT * FROM public.products",
            args = Tuple.tuple()
        )?.let { rows ->
            val total = rows.size()
            val items = if (rows.any()) rows.map { it.toProduct() } else listOf()
            Page(items, total)
        }

    override suspend fun delete(id: UUID): UpsertResult<String> =
        fetchRow(
            client = client,
            query = "DELETE * FROM public.products WHERE id = $1",
            args = Tuple.of(id)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not delete product with id: $id")
                else -> UpsertResult.Ok("Successfully deleted product with id: $id")
            }
        }

    override suspend fun create(productDBCreate: ProductDBCreate): UpsertResult<Product> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.products (name, price) VALUES ($1, $2)
                RETURNING *
            """.trimIndent(),
            args = Tuple.of(productDBCreate.name, productDBCreate.price)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not insert product: ${productDBCreate.name}")
                else -> UpsertResult.Ok(row.toProduct())
            }
        }

    override suspend fun update(productUpdate: ProductUpdate): UpsertResult<Product> =
        fetchRow(
            client = client,
            query = """
                UPDATE public.products SET 
                name = COALESCE($2, name), price = COALESCE($3, price)
                WHERE id = $1
                RETURNING *
            """.trimIndent(),
            args = Tuple.of(productUpdate.id, productUpdate.name, productUpdate.price)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not update product with id: ${productUpdate.id}")
                else -> UpsertResult.Ok(row.toProduct())
            }
        }

    override suspend fun addProductToStore(productToStoreTuple: Tuple): UpsertResult<String> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.products_stores (product_id, store_id, updated_at, expired_at, quantity)
                VALUES ($1, $2, $3, $4, $5) RETURNING *
            """.trimIndent(),
            args = productToStoreTuple
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to insert in entry for addStoreToProduct")
                else -> UpsertResult.Ok("Successfully inserted entry for addStoreToProduct")
            }
        }

    override suspend fun updateProductToStore(updateProductToStoreTuple: Tuple): UpsertResult<String> =
        fetchRow(
            client = client,
            query = """
                 UPDATE public.products_stores SET updated_at = $2, expired_at = $3, quantity = $4
                 WHERE product_id = $1
                 RETURNING *
            """.trimIndent(),
            args = updateProductToStoreTuple
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not update product to store timestamps..")
                else -> UpsertResult.Ok("Successfully updated product to store timestamps!")
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
