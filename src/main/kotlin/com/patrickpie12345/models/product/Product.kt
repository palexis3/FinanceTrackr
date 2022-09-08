package com.patrickpie12345.models.product

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.patrickpie12345.helper.FromNow
import com.patrickpie12345.models.store.StoreCreate
import java.time.Instant
import java.util.*

@GraphQLDescription("Product information to describe an item")
data class Product(
    val id: UUID,
    val name: String,
    val price: Float,
    val quantity: Int,
    val imageId: UUID? = null,
    val createdAt: Instant
)

@GraphQLDescription("Product object needed to create one")
data class ProductCreate(
    val name: String,
    val price: Float,
    val quantity: Int,
    val expirationFromNow: FromNow,
    val stores: List<StoreCreate>
)

data class ProductDBCreate(
    val name: String,
    val price: Float,
    val quantity: Int
)

data class ProductDBUpdate(
    val id: UUID,
    val name: String? = null,
    val price: Float? = null,
    val quantity: Int? = null
)
