package com.patrickpie12345.storage.receipts

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.Receipt
import com.patrickpie12345.models.receipt.ReceiptAnalyticsCategoryDBRequest
import com.patrickpie12345.models.receipt.ReceiptDBCreate
import com.patrickpie12345.models.receipt.StoreCategorySum
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ItemImageStorage
import io.vertx.sqlclient.Row
import java.time.ZoneOffset
import java.util.UUID

fun Row.toReceipt() = Receipt(
    id = this.getUUID("id"),
    title = this.getString("title"),
    price = this.getFloat("price"),
    storeId = this.getUUID("store_id"),
    createdAt = this.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC)
).also {
    it.imageUrl = this.getString("aws_s3_url")
}

interface ReceiptStorage : ItemImageStorage {
    suspend fun get(id: UUID): Receipt?
    suspend fun getAll(): Page<Receipt>?
    suspend fun create(newReceipt: ReceiptDBCreate): UpsertResult<Receipt>
    suspend fun getCategorySum(categoryDBRequest: ReceiptAnalyticsCategoryDBRequest): Page<StoreCategorySum>
}
