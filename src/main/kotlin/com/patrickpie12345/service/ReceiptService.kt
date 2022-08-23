package com.patrickpie12345.service

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.Receipt
import com.patrickpie12345.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.receipts.ReceiptStorage
import java.util.*

class ReceiptService(private val receiptStorage: ReceiptStorage) {

    suspend fun getAll(): Page<Receipt>? {
        val receiptWrapper = receiptStorage.getAll()
        return if (receiptWrapper != null && receiptWrapper.items.isNotEmpty()) {
            receiptWrapper
        } else {
            null
        }
    }

    suspend fun get(id: String): Receipt? {
        return when (val receipt = receiptStorage.get(UUID.fromString(id))) {
            null -> null
            else -> receipt
        }
    }

    suspend fun create(receiptCreate: ReceiptCreate): UpsertResult<Receipt> {
        return receiptStorage.create(receiptCreate)
    }
}
