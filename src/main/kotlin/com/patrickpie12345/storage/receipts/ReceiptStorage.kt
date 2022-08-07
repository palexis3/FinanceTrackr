package com.patrickpie12345.storage.receipts

import com.patrickpie12345.graphql.models.Receipt
import com.patrickpie12345.graphql.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import java.util.UUID

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