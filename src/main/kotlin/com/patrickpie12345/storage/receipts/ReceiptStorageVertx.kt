package com.patrickpie12345.storage.receipts

import com.patrickpie12345.helper.NumberConverter
import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.*
import com.patrickpie12345.models.store.StoreCategory
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import com.patrickpie12345.storage.VertxStorageExtension.fetchRowSet
import com.patrickpie12345.storage.images.ItemImageStorage
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.UUID

class ReceiptStorageVertx(private val client: SqlClient) : ReceiptStorage, ItemImageStorage {

    override suspend fun get(id: UUID): Receipt? =
        fetchRow(
            client = client,
            query = "SELECT * FROM public.receipts WHERE id = $1",
            args = Tuple.of(id)
        )?.toReceipt()

    override suspend fun getAll(): Page<Receipt>? =
        fetchRowSet(
            client = client,
            query = "SELECT * FROM public.receipts",
            args = Tuple.tuple()
        )?.let { rows ->
            val total = rows.size()
            val receipts = if (rows.any()) rows.map { it.toReceipt() } else listOf()
            Page(receipts, total)
        }

    override suspend fun create(newReceipt: ReceiptDBCreate): UpsertResult<Receipt> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.receipts (title, price, store_id) VALUES
                ($1, $2, $3) RETURNING *
            """.trimIndent(),
            args = Tuple.of(
                newReceipt.title, newReceipt.price, newReceipt.storeId
            )
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to insert new receipt for ${newReceipt.title}")
                else -> UpsertResult.Ok(row.toReceipt())
            }
        }

    override suspend fun addImageId(receiptId: UUID, imageId: UUID): UpsertResult<String> =
        fetchRow(
            client = client,
            query = """
                UPDATE public.receipts SET image_id = $2 WHERE id = $1 RETURNING image_id
            """.trimIndent(),
            args = Tuple.of(receiptId, imageId)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to update receiptId: $receiptId with image url")
                else -> UpsertResult.Ok("Successfully updated receiptId: $receiptId with the following imageId: $imageId")
            }
        }

    /**
     * The categories for each receipt is interpreted from the store associated with it that
     * can be fetched from the store_id column. To sum the prices in the left table (public.receipts)
     * based on that store id, there has to be an INNER JOIN  with the right table (public.stores)
     * to select the rows that have the same store_id from receipts with the id of the stores table that
     * has the desired category requested (if there isn't one, then all are selected)
     */
    override suspend fun getCategorySum(categoryDBRequest: ReceiptAnalyticsCategoryDBRequest): Page<StoreCategorySum> =
        fetchRowSet(
            client = client,
            query = """
                SELECT COALESCE(SUM(rec.price), 0) AS total, sto.store_category
                FROM public.receipts AS rec INNER JOIN public.stores AS sto
                ON rec.store_id = sto.id WHERE 
                sto.store_category = COALESCE(NULLIF($1::text, ''), sto.store_category::text)::store_category AND
                rec.created_at::date >= $2::date AND rec.created_at::date <= $3::date
                GROUP BY sto.store_category
            """.trimIndent(),
            args = Tuple.of(categoryDBRequest.category, categoryDBRequest.beginningDate, categoryDBRequest.endingDate)
        )?.let { rows ->
            val size = rows.size()
            val items = mutableListOf<StoreCategorySum>()
            for (row in rows) {
                items += StoreCategorySum(
                    category = row.get(StoreCategory::class.java, "store_category"),
                    total = NumberConverter.floatToDollarConversion(row.getFloat("total"))
                )
            }
            Page(items, size)
        } ?: Page(listOf(), 0)
}
