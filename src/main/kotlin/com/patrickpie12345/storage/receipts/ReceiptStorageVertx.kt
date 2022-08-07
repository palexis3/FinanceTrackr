package com.patrickpie12345.storage.receipts

import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import com.patrickpie12345.storage.receipts.ReceiptPostgresHelper.receiptReturn
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.*

class ReceiptStorageVertx(private val client: SqlClient): ReceiptStorage {

    override suspend fun get(id: UUID): Receipt? =
        fetchRow(
            client = client,
            query = "SELECT * from public.receipt where id = $1",
            args = Tuple.of(id)
        )?.toReceipt()


    override suspend fun create(newReceipt: ReceiptCreate): UpsertResult<Receipt> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.receipt (id, title, price, category, image_url, created_at) VALUES
                ($1, $2, $3, $4, $5, $6)
                RETURNING $receiptReturn
            """.trimIndent(),
            args = Tuple.of(newReceipt.id, newReceipt.title, newReceipt.price,
                newReceipt.category, newReceipt.imageUrl, newReceipt.createdAt)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Failed to insert new receipt for ${newReceipt.title}")
                else -> UpsertResult.Ok(row.toReceipt())
            }
        }
}