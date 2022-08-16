package com.patrickpie12345.graphql.mutations

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import com.patrickpie12345.graphql.models.Receipt
import com.patrickpie12345.graphql.models.ReceiptCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.receipts.ReceiptStorage
import org.koin.java.KoinJavaComponent.getKoin

class ReceiptMutation : Mutation {

    @GraphQLIgnore
    private val receiptStorage by getKoin().inject<ReceiptStorage>()
    @GraphQLDescription("Add new receipt")
    suspend fun createReceipt(receipt: ReceiptCreate): Receipt = run {
        when (val result = receiptStorage.create(receipt)) {
            is UpsertResult.Ok -> result.result
            else -> throw Exception("Unable to create receipt ${receipt.id})")
        }
    }
}
