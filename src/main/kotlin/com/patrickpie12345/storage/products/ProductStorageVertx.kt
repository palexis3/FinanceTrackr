package com.patrickpie12345.storage.products

import ProductCategoryDBAnalyticsRequest
import ProductCategorySum
import ProductStoreSum
import ProductStoresDBAnalyticsRequest
import com.patrickpie12345.models.Page
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductToStoreDBCreate
import com.patrickpie12345.models.product.ProductToStoreDBUpdate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import com.patrickpie12345.storage.VertxStorageExtension.fetchRowSet
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.UUID

class ProductStorageVertx(private val client: SqlClient) : ProductStorage {

    override suspend fun get(id: UUID): Product? =
        fetchRow(
            client = client,
            query = """
                SELECT products.*, images.aws_s3_url FROM public.products AS pro LEFT JOIN public.images AS ima
                ON pro.id = $1 AND pro.image_id = ima.id
            """.trimIndent(),
            args = Tuple.of(id)
        )?.toProduct()

    override suspend fun getByCategory(productCategory: String): Page<Product> =
        fetchRowSet(
            client = client,
            query = "SELECT * FROM public.products WHERE product_category = $1",
            args = Tuple.of(productCategory)
        )?.let { rows ->
            val total = rows.size()
            val items = if (rows.any()) rows.map { it.toProduct() } else listOf()
            Page(items, total)
        } ?: Page(listOf(), 0)

    override suspend fun getByStore(storeId: UUID): Page<Product> =
        fetchRowSet(
            client = client,
            query = """
                SELECT * FROM public.products AS pro LEFT JOIN public.products_stores AS pro_sto
                ON pro.id = pro_sto.product_id
                WHERE pro_sto.store_id IN (
                    SELECT store_id FROM public.products_stores WHERE store_id = $1
                )
            """.trimIndent(),
            args = Tuple.of(storeId)
        )?.let { rows ->
            val total = rows.size()
            val items = if (rows.any()) rows.map { it.toProduct() } else listOf()
            Page(items, total)
        } ?: Page(listOf(), 0)

    override suspend fun getAll(): Page<Product>? =
        fetchRowSet(
            client = client,
            query = """
                SELECT products.*, images.aws_s3_url FROM public.products AS pro LEFT JOIN public.images AS ima
                ON pro.image_id = ima.id
            """.trimIndent(),
            args = Tuple.tuple()
        )?.let { rows ->
            val total = rows.size()
            val items = if (rows.any()) rows.map { it.toProduct() } else listOf()
            Page(items, total)
        }

    override suspend fun delete(id: UUID): UpsertResult<String> =
        fetchRow(
            client = client,
            query = "DELETE FROM public.products WHERE id = $1 RETURNING *",
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
                INSERT INTO public.products (name, price, product_category) VALUES ($1, $2, $3)
                RETURNING *
            """.trimIndent(),
            args = Tuple.of(productDBCreate.name, productDBCreate.price, productDBCreate.category.canonicalName)
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

    override suspend fun addProductToStore(productToStoreDBCreate: ProductToStoreDBCreate): UpsertResult<String> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.products_stores (product_id, store_id, updated_at, expired_at, quantity)
                VALUES ($1, $2, $3, $4, $5) RETURNING *
            """.trimIndent(),
            args = Tuple.of(
                productToStoreDBCreate.productId, productToStoreDBCreate.storeId,
                productToStoreDBCreate.startOffsetDate, productToStoreDBCreate.endOffsetDate,
                productToStoreDBCreate.quantity
            )
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to insert in entry for addStoreToProduct")
                else -> UpsertResult.Ok("Successfully inserted entry for addStoreToProduct")
            }
        }

    override suspend fun updateProductToStore(productToStoreDBUpdate: ProductToStoreDBUpdate): UpsertResult<String> =
        fetchRow(
            client = client,
            query = """
                 UPDATE public.products_stores SET updated_at = $2, expired_at = $3, quantity = $4
                 WHERE product_id = $1
                 RETURNING *
            """.trimIndent(),
            args = Tuple.of(
                productToStoreDBUpdate.productId, productToStoreDBUpdate.startOffsetDate,
                productToStoreDBUpdate.endOffsetDate, productToStoreDBUpdate.quantity
            )
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not update product to store timestamps..")
                else -> UpsertResult.Ok("Successfully updated product to store timestamps!")
            }
        }

    override suspend fun deleteProductToStores(productId: UUID): UpsertResult<String> =
        fetchRow(
            client = client,
            query = "DELETE FROM public.products_stores WHERE product_id = $1 RETURNING *",
            args = Tuple.of(productId)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Deleted the product stores relations with productId: $productId")
                else -> UpsertResult.Ok("Could not delete the product stores relations with productId: $productId")
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

    override suspend fun getCategorySum(productCategoryRequest: ProductCategoryDBAnalyticsRequest): Page<ProductCategorySum> =
        fetchRowSet(
            client = client,
            query = """
                SELECT COALESCE(SUM(pro.price), 0) AS total, pro.product_category
                FROM public.products AS pro LEFT JOIN public.product_categories AS cat
                    ON pro.product_category = cat.name
                       WHERE cat.name IN (
                            SELECT name FROM public.product_categories WHERE path <@ (
                                SELECT path FROM public.product_categories
                                WHERE name = COALESCE($3, 'Root')
                            )
                    )
                   AND
                   pro.created_at::date >= $1::date AND pro.created_at::date <= $2::date
                GROUP BY pro.product_category;
            """.trimIndent(),
            args = Tuple.of(productCategoryRequest.startDate, productCategoryRequest.endDate, productCategoryRequest.category)
        )?.let { rows ->
            val size = rows.size()
            val items = mutableListOf<ProductCategorySum>()
            for (row in rows) {
                items += ProductCategorySum(
                    productCategory = row.getString("product_category"),
                    total = row.getFloat("total")
                )
            }
            Page(items, size)
        } ?: Page(listOf(), 0)

    override suspend fun getStoreSum(productStoreRequest: ProductStoresDBAnalyticsRequest): Page<ProductStoreSum> =
        fetchRowSet(
            client = client,
            query = """
                SELECT COALESCE(SUM(pro.price), 0) AS total, sto.id as store_id
                FROM public.products AS pro
                    INNER JOIN public.products_stores AS pro_sto
                      ON pro.id = pro_sto.product_id
                        AND pro_sto.updated_at::date >= $1::date AND pro_sto.updated_at::date <= $2::date
                    INNER JOIN public.stores AS sto
                      ON pro_sto.store_id = sto.id
                        WHERE sto.id IN (
                            SELECT id FROM public.stores WHERE name = $3 OR $3 IS NULL
                        )
                GROUP BY sto.id
            """.trimIndent(),
            args = Tuple.of(productStoreRequest.startDate, productStoreRequest.endDate, productStoreRequest.store)
        )?.let { rows ->
            val size = rows.size()
            val items = mutableListOf<ProductStoreSum>()
            for (row in rows) {
                items += ProductStoreSum(
                    storeId = row.getUUID("store_id"),
                    total = row.getFloat("total")
                )
            }
            Page(items, size)
        } ?: Page(listOf(), 0)
}
