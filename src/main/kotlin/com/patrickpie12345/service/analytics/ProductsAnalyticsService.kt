package com.patrickpie12345.service.analytics

import com.patrickpie12345.helper.NumberConverter
import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.models.product.*
import com.patrickpie12345.storage.products.ProductStorageVertx
import com.patrickpie12345.storage.stores.StoresStorageVertx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsAnalyticsService(
    private val productStorage: ProductStorageVertx,
    private val storeStorage: StoresStorageVertx
) {

    suspend fun getCategorySum(analyticsRequest: ProductCategoryAnalyticsRequest): ProductCategoryAnalyticsResponse =
        withContext(Dispatchers.IO) {
            val offsetDateRange = TimeDateConverter.getPastOffsetDateRange(analyticsRequest.timeToSearch)
            val dbRequest = ProductCategoryDBAnalyticsRequest(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate,
                analyticsRequest.category?.canonicalName
            )
            val readableDateRange = TimeDateConverter.getReadableDateRange(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate
            )

            val categoryAndProductsList = mutableListOf<CategoryAndProducts>()
            val result = productStorage.getCategorySum(dbRequest)
            result.items.forEach { productCategorySum ->
                categoryAndProductsList.add(
                    withContext(Dispatchers.IO) {
                        val products = productStorage.get(productCategorySum.productCategory)?.items ?: listOf()
                        val total = NumberConverter.floatToDollarConversion(productCategorySum.total)
                        CategoryAndProducts(productCategorySum.productCategory, products, total)
                    }
                )
            }
            ProductCategoryAnalyticsResponse(readableDateRange.startDate, readableDateRange.endDate, categoryAndProductsList)
        }

    suspend fun getStoreSum(analyticsRequest: ProductStoresAnalyticsRequest): ProductStoresAnalyticsResponse =
        withContext(Dispatchers.IO) {
            val offsetDateRange = TimeDateConverter.getPastOffsetDateRange(analyticsRequest.timeToSearch)
            val dbRequest = ProductStoresDBAnalyticsRequest(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate,
                analyticsRequest.store
            )
            val readableDateRange = TimeDateConverter.getReadableDateRange(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate
            )

            val storesAndProducts = mutableListOf<StoreAndProducts>()
            val result = productStorage.getStoreSum(dbRequest)

            result.items.forEach { productStoreSum ->
                storesAndProducts.add(
                    withContext(Dispatchers.IO) {
                        val store = storeStorage.get(productStoreSum.storeId)
                        val total = NumberConverter.floatToDollarConversion(productStoreSum.total)
                        // TODO: Get list of products associated with the store id
                        val products = productStorage.getAll()?.items
                        StoreAndProducts(store, products, total)
                    }
                )

            }
            ProductStoresAnalyticsResponse(readableDateRange.startDate, readableDateRange.endDate, storesAndProducts)
        }
}
