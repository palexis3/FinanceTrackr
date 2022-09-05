package com.patrickpie12345.models.receipt

import com.patrickpie12345.helper.TimeToSearch
import java.time.OffsetDateTime

data class ReceiptAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val category: StoreCategory? = null
)

data class ReceiptAnalyticsCategoryDBRequest(
    val beginningDate: OffsetDateTime,
    val endingDate: OffsetDateTime,
    val category: StoreCategory?
)

data class ReceiptAnalyticsCategoryResponse(
    val startDate: String,
    val endDate: String,
    val categories: List<CategoryItem>
)

data class CategoryItem(
    val category: StoreCategory,
    val total: Float
)
