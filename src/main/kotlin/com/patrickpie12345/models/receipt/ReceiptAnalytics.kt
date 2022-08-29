package com.patrickpie12345.models.receipt

import com.patrickpie12345.helper.TimeToSearch
import java.time.OffsetDateTime

data class ReceiptAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val category: Category? = null
)

data class ReceiptAnalyticsCategoryDBRequest(
    val beginningDate: OffsetDateTime,
    val category: Category?
)

data class ReceiptAnalyticsCategoryResponse(
    val startDate: String,
    val endDate: String,
    val categories: List<CategoryItem>
)

data class CategoryItem(
    val category: Category,
    val total: Float
)
