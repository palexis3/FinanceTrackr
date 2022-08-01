package com.patrickpie12345.storage.receipts

import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import java.time.Instant
import java.util.UUID

enum class Category { RESTAURANT, GROCERY, HOUSEHOLD, MISCELLANOUS }

data class Receipt(
    val id: UUID,
    val title: String,
    val price: Float,
    val category: Category? = null,
    val imageUrl: String?,
    val createdAt: Instant?= null
)

data class ReceiptCreate(
    val id: UUID,
    val title: String,
    val price: Float,
    val category: Category?,
    val imageUrl: String?,
    val createdAt: Instant
)

fun Row.toReceipt() = Receipt(
    id = getUUID("id"),
    title = getString("title"),
    price = getFloat("price"),
    imageUrl = getString("image_url")
)

interface ReceiptStorage {
    suspend fun get(id: UUID): Receipt?
    suspend fun create(newReceipt: ReceiptCreate): UpsertResult<Receipt>
}