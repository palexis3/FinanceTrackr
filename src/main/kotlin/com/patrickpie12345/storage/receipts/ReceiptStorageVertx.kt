package com.patrickpie12345.storage.receipts

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.Receipt
import com.patrickpie12345.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import com.patrickpie12345.storage.VertxStorageExtension.fetchRowSet
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.UUID

class ReceiptStorageVertx(private val client: SqlClient) : ReceiptStorage {

    override suspend fun get(id: UUID): Receipt? =
        fetchRow(
            client = client,
            query = "SELECT * FROM public.receipt WHERE id = $1",
            args = Tuple.of(id)
        )?.toReceipt()

    override suspend fun getAll(): Page<Receipt>? =
        fetchRowSet(
            client = client,
            query = "SELECT * FROM public.receipt",
            args = Tuple.tuple()
        )?.let { rows ->
            val total = rows.size()
            val receipts = if (rows.any()) rows.map { it.toReceipt() } else listOf()
            Page(receipts, total)
        }

    override suspend fun create(newReceipt: ReceiptCreate): UpsertResult<Receipt> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.receipt (title, price, category) VALUES
                ($1, $2, $3) RETURNING *
            """.trimIndent(),
            args = Tuple.of(
                newReceipt.title, newReceipt.price,
                newReceipt.category
            )
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to insert new receipt for ${newReceipt.title}")
                else -> UpsertResult.Ok(row.toReceipt())
            }
        }

    override suspend fun addImage(receiptId: UUID, imageUrl: String): UpsertResult<String> =
        fetchRow(
            client = client,
            query = """
                UPDATE public.receipt SET image_url = $2 WHERE id = $1
            """.trimIndent(),
            args = Tuple.of(receiptId, imageUrl)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to update receiptId: $receiptId with image url")
                else -> UpsertResult.Ok("Successfully updated receiptId: $receiptId with the following imageUrl: $imageUrl")
            }
        }
}
