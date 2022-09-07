package com.patrickpie12345.server.graphql.mutations

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.patrickpie12345.models.receipt.Receipt
import com.patrickpie12345.models.receipt.ReceiptCreate
import com.patrickpie12345.models.receipt.ReceiptDBCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.receipts.ReceiptStorage
import com.patrickpie12345.storage.stores.StoresStorage
import org.koin.java.KoinJavaComponent.getKoin

class ReceiptMutation : Mutation {

    private val receiptStorage by getKoin().inject<ReceiptStorage>()
    private val storesStorage by getKoin().inject<StoresStorage>()

    @GraphQLDescription("Add new receipt")
    suspend fun createReceipt(receipt: ReceiptCreate): Receipt? = run {
        val receiptUpsertResult = when (val storeUpsertResult = storesStorage.saveStore(receipt.store)) {
            is UpsertResult.Ok -> {
                val receiptDBCreate = ReceiptDBCreate(
                    title = receipt.title,
                    price = receipt.price,
                    storeId = storeUpsertResult.result.id
                )
                receiptStorage.create(receiptDBCreate)
            }
            else -> UpsertResult.NotOk("Could not create receipt: ${receipt.title}")
        }

        when (receiptUpsertResult) {
            is UpsertResult.Ok -> receiptUpsertResult.result
            else -> null
        }
    }
}
