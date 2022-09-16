package com.patrickpie12345.models.product

import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.store.Store
import java.time.OffsetDateTime

data class ProductCategoryAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val category: ProductCategory? = null
)

data class ProductCategoryDBAnalyticsRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val category: ProductCategory?
)

data class ProductCategoryAnalyticsResponse(
    val startDate: String,
    val endDate: String,
    val items: List<CategoryAndProducts>
)

data class CategoryAndProducts(
    val productCategory: String,
    val products: List<Product>,
    val total: Float
)

data class ProductCategorySum(
    val productCategory: String,
    val total: Float
)

data class ProductStoresAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val store: Store? = null
)

data class ProductStoresDBAnalyticsRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val store: Store?
)

data class ProductStoresAnalyticsResponse(
    val startDate: String,
    val endDate: String,
    val items: List<StoreAndProducts>
)

data class StoreAndProducts(
    val store: Store,
    val products: List<Product>,
    val total: Float
)

data class ProductStoreSum(
    val store: Store,
    val total: Float
)
