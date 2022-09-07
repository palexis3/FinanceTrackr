package com.patrickpie12345.service

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.Receipt
import com.patrickpie12345.models.receipt.ReceiptCreate
import com.patrickpie12345.models.receipt.ReceiptDBCreate
import com.patrickpie12345.service.aws.AwsStorageService
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ImageStorage
import com.patrickpie12345.storage.receipts.ReceiptStorage
import com.patrickpie12345.storage.stores.StoresStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class ReceiptService(
    private val receiptStorage: ReceiptStorage,
    private val awsStorageService: AwsStorageService,
    private val imageStorage: ImageStorage,
    private val storesStorage: StoresStorage
) {

    suspend fun getAll(): Page<Receipt>? {
        val receiptPage = receiptStorage.getAll()
        return if (receiptPage != null && receiptPage.items.isNotEmpty()) {
            receiptPage
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

    suspend fun create(receiptCreate: ReceiptCreate): UpsertResult<Receipt> =
        withContext(Dispatchers.IO) {
            when (val storeUpsertResult = storesStorage.saveStore(receiptCreate.store)) {
                is UpsertResult.Ok -> {
                    val receiptDBCreate = ReceiptDBCreate(
                        title = receiptCreate.title,
                        price = receiptCreate.price,
                        storeId = storeUpsertResult.result.id
                    )
                    receiptStorage.create(receiptDBCreate)
                }
                else -> UpsertResult.NotOk("Could not create receipt: ${receiptCreate.title}")
            }
        }

    suspend fun addImage(receiptId: String, image: File): UpsertResult<String> =
        withContext(Dispatchers.IO) {
            val imageUrl = awsStorageService.save(image)
            when (val imageIdUpsertResult = imageStorage.addImage(imageUrl)) {
                is UpsertResult.Ok -> receiptStorage.addImageId(
                    receiptId = UUID.fromString(receiptId),
                    imageId = imageIdUpsertResult.result
                )
                else -> UpsertResult.NotOk("Error inserting image for receipt: $receiptId")
            }
        }
}
