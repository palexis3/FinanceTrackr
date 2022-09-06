package com.patrickpie12345.models.store

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.util.*

@GraphQLDescription("StoreCategory is used to describe what type of establishment we got this receipt from")
enum class StoreCategory { GROCERY, SPECIALTY, DEPARTMENT, WAREHOUSE, DISCOUNT, CONVENIENCE, RESTAURANT }

@GraphQLDescription("Store is defined as a location where some good or service is received")
data class Store(
    val id: UUID,
    val name: String,
    val category: StoreCategory
)

@GraphQLDescription("Store data object to create one")
data class StoreCreate(
    val name: String,
    val category: StoreCategory
)
