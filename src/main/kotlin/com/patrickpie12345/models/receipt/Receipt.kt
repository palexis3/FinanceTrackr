package com.patrickpie12345.models.receipt

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.patrickpie12345.models.store.StoreCreate
import java.time.Instant
import java.util.*

@GraphQLDescription("Receipt information to describe purchase(s)")
data class Receipt(
    val id: UUID,
    val title: String,
    val price: Float,
    val imageId: UUID? = null,
    val storeId: UUID,
    val createdAt: Instant
) {
    var imageUrl: String? = null
}

@GraphQLDescription("Receipt data object to create one")
data class ReceiptCreate(
    val title: String,
    val price: Float,
    val store: StoreCreate
)

data class ReceiptDBCreate(
    val title: String,
    val price: Float,
    val storeId: UUID
)
