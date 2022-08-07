package com.patrickpie12345.graphql.mutations

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.patrickpie12345.graphql.models.Receipt
import com.patrickpie12345.graphql.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.receipts.ReceiptStorage

class ReceiptMutation(@GraphQLIgnore val receiptStorage: ReceiptStorage) {

    @GraphQLDescription("Add new receipt")
    suspend fun createReceipt(receipt: ReceiptCreate): Receipt = run {
        when(val result = receiptStorage.create(receipt)) {
            is UpsertResult.Ok -> result.result
            else -> throw Exception("Unable to create receipt ${receipt.id})")
        }
    }
}