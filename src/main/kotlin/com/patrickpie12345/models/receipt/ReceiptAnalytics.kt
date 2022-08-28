package com.patrickpie12345.models.receipt

import com.patrickpie12345.helper.TimeToSearch

data class ReceiptAnalyticsRequest(
    val timeToSearch: TimeToSearch? = null,
    val category: Category? = null
)

data class ReceiptAnalyticsResponse(
    val dateRange: String,
    val total: Float
)
