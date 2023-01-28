package com.patrickpie12345.models.product

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.patrickpie12345.helper.FromNow
import com.patrickpie12345.models.store.StoreCreate
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

@GraphQLDescription("ProductCategory is used to describe an individual item that can be bought and used")
enum class ProductCategory(val canonicalName: String) {
    BEAUTY("Beauty"),
    BATH_AND_BODY("Bath & Body"),
    FRAGRANCES("Fragrances"),
    SKIN_CARE(" Skin Care"),
    SUNSCREEN("Sunscreen"),
    CLOTHING("Clothing"),
    ELECTRONICS("Electronics"),
    GROCERY("Grocery"),
    BEVERAGES("Beverages"),
    PANTRY("Pantry"),
    SEAFOOD("Seafood"),
    POULTRY("Poultry"),
    DAIRY("Dairy"),
    COLD_AND_FROZEN("Cold & Frozen"),
    HOUSEHOLD_ESSENTIALS("Household Essentials"),
    CLEANING_SUPPLIES("Cleaning Supplies"),
    LAUNDRY("Laundry"),
    PAPER_AND_PLASTICS("Paper & Plastics"),
    HEALTH_AND_PERSONAL_CARE("Health & Personal Care"),
    NUTRITION("Nutrition"),
    PROTEIN_SHAKE("Protein Shake"),
    PERSONAL_CARE("Personal Care"),
    CLEANSING_WIPES("Cleansing Wipes"),
    DEODORANT("Deodorant"),
    HAIR_CARE("Hair Care"),
    MOISTURIZER("Moisturizer"),
    ORAL_CARE("Oral  Care"),
    SHAVING("Shaving"),
    SOAP("Soap"),
    FRUITS("Fruits"),
    VEGETABLES("Vegetables")
}

@GraphQLDescription("Product information to describe an item")
data class Product(
    val id: UUID,
    val name: String,
    val price: String,
    val imageId: UUID? = null,
    val createdAt: Instant,
    val productCategory: String
) {
    var imageUrl: String? = null
}

@GraphQLDescription("Product object needed to create one")
data class ProductCreate(
    val name: String,
    val price: Float,
    val productExpiration: ProductExpiration,
    val store: StoreCreate,
    val category: ProductCategory
)

data class ProductDBCreate(
    val name: String,
    val price: Float,
    val category: ProductCategory
)

data class ProductToStoreDBCreate(
    val productId: UUID,
    val storeId: UUID?,
    val startOffsetDate: OffsetDateTime,
    val endOffsetDate: OffsetDateTime,
    val quantity: Int
)

data class ProductToStoreDBUpdate(
    val productId: UUID,
    val startOffsetDate: OffsetDateTime,
    val endOffsetDate: OffsetDateTime,
    val quantity: Int
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
