package com.patrickpie12345.models.receipt

import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.store.StoreCategory
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
    val categories: List<StoreCategorySum>
)

data class StoreCategorySum(
    val category: StoreCategory,
    val total: String
)
