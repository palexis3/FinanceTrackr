package com.patrickpie12345.graphql.models

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import java.time.Instant
import java.util.*

@GraphQLDescription("Category is used to describe what item(s) are purchased in the receipt")
enum class Category { RESTAURANT, GROCERY, HOUSEHOLD, MISCELLANOUS }

@GraphQLDescription("Receipt information to describe purchases")
data class Receipt(
    val id: UUID,
    val title: String,
    val price: Float,
    val category: Category? = null,
    val imageUrl: String?,
    val createdAt: Instant?= null
)

data class ReceiptCreate(
    @GraphQLIgnore val id: UUID = UUID.randomUUID(),
    val title: String,
    val price: Float,
    val category: Category?,
    val imageUrl: String?,
    val createdAt: Instant
)