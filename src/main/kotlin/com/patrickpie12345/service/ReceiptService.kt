package com.patrickpie12345.service

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.Receipt
import com.patrickpie12345.models.receipt.ReceiptCreate
import com.patrickpie12345.service.aws.FileStorageService
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ImageStorage
import com.patrickpie12345.storage.receipts.ReceiptStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class ReceiptService(
    private val receiptStorage: ReceiptStorage,
    private val fileStorageService: FileStorageService,
    private val imageStorage: ImageStorage
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

    // TODO: Include store info with receipt where we'll query based on the name and category of the store
    suspend fun create(receiptCreate: ReceiptCreate): UpsertResult<Receipt> {
        return receiptStorage.create(receiptCreate)
    }

    suspend fun addImage(receiptId: String, image: File): UpsertResult<String> =
        withContext(Dispatchers.IO) {
            val imageUrl = fileStorageService.save(image)
            when (val imageIdUpsertResult = imageStorage.addImage(imageUrl)) {
                is UpsertResult.Ok -> receiptStorage.addImageId(
                    receiptId = UUID.fromString(receiptId),
                    imageId = imageIdUpsertResult.result
                )
                else -> UpsertResult.NotOk("Error inserting image for receipt: $receiptId")
            }
        }
}
