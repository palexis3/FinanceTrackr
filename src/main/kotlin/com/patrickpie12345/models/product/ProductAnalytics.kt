package com.patrickpie12345.models.product

import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.store.Store
import java.time.OffsetDateTime
import java.util.*

data class ProductCategoryAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val category: ProductCategory? = null
)

data class ProductCategoryDBAnalyticsRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val category: String?
)

data class ProductCategoryAnalyticsResponse(
    val startDate: String,
    val endDate: String,
    val items: List<CategoryAndProducts>
)

data class CategoryAndProducts(
    val productCategory: String,
    val products: List<Product>,
    val total: String
)

data class ProductCategorySum(
    val productCategory: String,
    val total: Float
)

data class ProductStoresAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val store: String? = null
)

data class ProductStoresDBAnalyticsRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val store: String?
)

data class ProductStoresAnalyticsResponse(
    val startDate: String,
    val endDate: String,
    val items: List<StoreAndProducts>
)

data class StoreAndProducts(
    val store: Store,
    val products: List<Product>,
    val total: String
)

data class ProductStoreSum(
    val storeId: UUID,
    val total: Float
)
