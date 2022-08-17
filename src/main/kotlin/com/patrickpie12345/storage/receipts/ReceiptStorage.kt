package com.patrickpie12345.storage.receipts

import com.patrickpie12345.Page
import com.patrickpie12345.graphql.models.Receipt
import com.patrickpie12345.graphql.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import java.util.UUID

fun Row.toReceipt() = Receipt(
    id = this.getUUID("id"),
    title = this.getString("title"),
    price = this.getFloat("price"),
    imageUrl = this.getString("image_url"),
    createdAt = this.getOffsetDateTime("created_at").toInstant()
)

interface ReceiptStorage {
    suspend fun get(id: UUID): Receipt?
    suspend fun getAll(): Page<Receipt>?
    suspend fun create(newReceipt: ReceiptCreate): UpsertResult<Receipt>
}
