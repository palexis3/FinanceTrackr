package com.patrickpie12345.server.graphql.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import com.patrickpie12345.models.Receipt
import com.patrickpie12345.storage.receipts.ReceiptStorage
import org.koin.java.KoinJavaComponent.getKoin
import java.util.UUID

class ReceiptQuery : Query {

    private val receiptStorage by getKoin().inject<ReceiptStorage>()

    @GraphQLDescription("Get the receipt with the particular id")
    suspend fun getReceipt(id: String): Receipt? = receiptStorage.get(UUID.fromString(id))
}
