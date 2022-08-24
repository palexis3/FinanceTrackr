package com.patrickpie12345.service

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.Receipt
import com.patrickpie12345.models.ReceiptCreate
import com.patrickpie12345.service.aws.FileStorageService
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.receipts.ReceiptStorage
import java.io.File
import java.util.UUID

class ReceiptService(
    private val receiptStorage: ReceiptStorage,
    private val fileStorageService: FileStorageService
) {

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

    suspend fun addImage(receiptId: String, image: File): UpsertResult<String> {
        val imageUrl = fileStorageService.save(image)
        return receiptStorage.addImage(receiptId = UUID.fromString(receiptId), imageUrl)
    }
}
