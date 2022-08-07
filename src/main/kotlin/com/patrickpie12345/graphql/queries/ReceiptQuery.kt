package com.patrickpie12345.graphql.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.patrickpie12345.graphql.models.Receipt
import com.patrickpie12345.storage.receipts.ReceiptStorage
import java.util.UUID

class ReceiptQuery(@GraphQLIgnore val receiptStorage: ReceiptStorage) {
    @GraphQLDescription("Get the receipt with the particular id")
    suspend fun getReceipt(id: String): Receipt? = receiptStorage.get(UUID.fromString(id))
}