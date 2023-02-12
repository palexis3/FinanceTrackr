package com.patrickpie12345.service

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.Receipt
import com.patrickpie12345.models.receipt.ReceiptCreate
import com.patrickpie12345.models.receipt.ReceiptDBCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ImagesTablesStorage
import com.patrickpie12345.storage.receipts.ReceiptStorage
import com.patrickpie12345.storage.stores.StoresStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ReceiptService(
    private val receiptStorage: ReceiptStorage,
    private val storesStorage: StoresStorage
) : ItemService(receiptStorage) {

    suspend fun getAll(): Page<Receipt>? = withContext(Dispatchers.IO) {
        val receiptPage = receiptStorage.getAll()
        if (receiptPage != null && receiptPage.items.isNotEmpty()) {
            receiptPage
        } else {
            null
        }
    }

    suspend fun get(id: String): Receipt? = withContext(Dispatchers.IO) {
        when (val receipt = receiptStorage.get(UUID.fromString(id))) {
            null -> null
            else -> {
//                val imageId = receipt.imageId
//                val imageUrl = imageId?.let { imagesTablesStorage.getImageUrl(imageId) } ?: ""
//                receipt.imageUrl = imageUrl
                receipt
            }
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
}
