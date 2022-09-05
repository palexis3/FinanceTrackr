package com.patrickpie12345.models.receipt

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.time.Instant
import java.util.*

@GraphQLDescription("StoreCategory is used to describe what type of establishment we got this receipt from")
enum class StoreCategory { GROCERY, SPECIALTY, DEPARTMENT, WAREHOUSE, DISCOUNT, CONVENIENCE, RESTAURANT }

@GraphQLDescription("Receipt information to describe purchases")
data class Receipt(
    val id: UUID,
    val title: String,
    val price: Float,
    val imageId: UUID? = null,
    val storeId: UUID? = null,
    val createdAt: Instant
)

data class ReceiptCreate(
    val title: String,
    val price: Float,
    val storeName: String,
    val storeCategory: StoreCategory
)
