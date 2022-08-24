package com.patrickpie12345.storage.receipts

import com.patrickpie12345.models.Category
import com.patrickpie12345.models.Page
import com.patrickpie12345.models.Receipt
import com.patrickpie12345.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import java.time.ZoneOffset
import java.util.UUID

fun Row.toReceipt() = Receipt(
    id = this.getUUID("id"),
    title = this.getString("title"),
    price = this.getFloat("price"),
    imageUrl = this.getString("image_url"),
    category = this.get(Category::class.java, "category"),
    createdAt = this.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC)
)

interface ReceiptStorage {
    suspend fun get(id: UUID): Receipt?
    suspend fun getAll(): Page<Receipt>?
    suspend fun create(newReceipt: ReceiptCreate): UpsertResult<Receipt>
    suspend fun addImage(receiptId: UUID, imageUrl: String): UpsertResult<String>
}
