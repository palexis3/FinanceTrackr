package com.patrickpie12345.service.analytics

import CategoryAndProducts
import ProductCategoryAnalyticsRequest
import ProductCategoryAnalyticsResponse
import ProductCategoryDBAnalyticsRequest
import ProductStoresAnalyticsRequest
import ProductStoresAnalyticsResponse
import ProductStoresDBAnalyticsRequest
import StoreAndProducts
import com.patrickpie12345.helper.NumberConverter
import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.storage.products.ProductStorage
import com.patrickpie12345.storage.stores.StoresStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsAnalyticsService(
    private val productStorage: ProductStorage,
    private val storeStorage: StoresStorage
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
                        val products = productStorage.getByCategory(productCategorySum.productCategory).items
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
                        val products = productStorage.getByStore(productStoreSum.storeId).items
                        StoreAndProducts(store, products, total)
                    }
                )
            }
            ProductStoresAnalyticsResponse(readableDateRange.startDate, readableDateRange.endDate, storesAndProducts)
        }
}
