package com.patrickpie12345.storage.receipts

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.*
import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import java.time.ZoneOffset
import java.util.UUID

fun Row.toReceipt() = Receipt(
    id = this.getUUID("id"),
    title = this.getString("title"),
    price = this.getFloat("price"),
    imageId = this.getUUID("image_id"),
    storeId = this.getUUID("store_id"),
    createdAt = this.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC)
)

interface ReceiptStorage {
    suspend fun get(id: UUID): Receipt?
    suspend fun getAll(): Page<Receipt>?
    suspend fun create(newReceipt: ReceiptDBCreate): UpsertResult<Receipt>
    suspend fun getCategorySum(categoryDBRequest: ReceiptAnalyticsCategoryDBRequest): Page<StoreCategorySum>
}
