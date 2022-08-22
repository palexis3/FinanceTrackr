package com.patrickpie12345.models

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.time.Instant
import java.util.*

@GraphQLDescription("Category is used to describe what item(s) are purchased in the receipt")
enum class Category { RESTAURANT, GROCERY, HOUSEHOLD, MISCELLANOUS }

@GraphQLDescription("Receipt information to describe purchases")
data class Receipt(
    val id: UUID,
    val title: String,
    val price: Float,
    val category: Category,
    val imageUrl: String? = null,
    val createdAt: Instant
)
data class ReceiptCreate(
    val title: String,
    val price: Float,
    val category: Category?,
    val imageUrl: String?
)
