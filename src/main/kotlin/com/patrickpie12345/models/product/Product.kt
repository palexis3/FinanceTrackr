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
    val imageId: UUID? = null,
    val createdAt: Instant
)

@GraphQLDescription("Product object needed to create one")
data class ProductCreate(
    val name: String,
    val price: Float,
    val quantity: Int,
    val expirationFromNow: FromNow,
    val store: StoreCreate
)

data class ProductDBCreate(
    val name: String,
    val price: Float
)

@GraphQLDescription("Product object to update one")
data class ProductUpdate(
    val id: UUID,
    val name: String? = null,
    val price: Float? = null,
    val productExpiration: ProductExpiration? = null
)

data class ProductExpiration(
    val quantity: Int,
    val expirationFromNow: FromNow
)
