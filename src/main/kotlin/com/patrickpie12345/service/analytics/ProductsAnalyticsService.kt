package com.patrickpie12345.service.analytics

import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.models.product.*
import com.patrickpie12345.storage.products.ProductStorageVertx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsAnalyticsService(
    private val productStorage: ProductStorageVertx
) {

    suspend fun getCategorySum(analyticsRequest: ProductCategoryAnalyticsRequest): ProductCategoryAnalyticsResponse =
        withContext(Dispatchers.IO) {
            val offsetDateRange = TimeDateConverter.getPastOffsetDateRange(analyticsRequest.timeToSearch)
            val dbRequest = ProductCategoryDBAnalyticsRequest(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate,
                analyticsRequest.category
            )
            val readableDateRange = TimeDateConverter.getReadableDateRange(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate
            )

            val categoryAndProductsList = mutableListOf<CategoryAndProducts>()
            val productCategorySum = productStorage.getCategorySum(dbRequest)

            productCategorySum.items.forEach { categorySum ->
                categoryAndProductsList.add(
                    withContext(Dispatchers.IO) {
                        val products = productStorage.get(categorySum.productCategory)?.items ?: listOf()
                        CategoryAndProducts(categorySum.productCategory, products, categorySum.total)
                    }
                )
            }
            ProductCategoryAnalyticsResponse(readableDateRange.startDate, readableDateRange.endDate, categoryAndProductsList)
        }
}
