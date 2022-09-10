package com.patrickpie12345.service

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.receipt.Receipt
import com.patrickpie12345.models.receipt.ReceiptCreate
import com.patrickpie12345.models.receipt.ReceiptDBCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.receipts.ReceiptStorageVertx
import com.patrickpie12345.storage.stores.StoresStorageVertx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ReceiptService(
    private val receiptStorage: ReceiptStorageVertx,
    private val storesStorage: StoresStorageVertx
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
}
