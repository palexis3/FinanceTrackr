package com.patrickpie12345.service.analytics

import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.models.receipt.*
import com.patrickpie12345.storage.receipts.ReceiptStorageVertx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReceiptAnalyticsService(
    private val receiptStorage: ReceiptStorageVertx
) {

    suspend fun getCategorySum(request: ReceiptAnalyticsRequest): ReceiptAnalyticsCategoryResponse =
        withContext(Dispatchers.IO) {
            val offsetDateRange = TimeDateConverter.getPastOffsetDateRange(request.timeToSearch)
            val dbRequest = ReceiptAnalyticsCategoryDBRequest(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate,
                request.category
            )

            val categoryItem = receiptStorage.getCategorySum(dbRequest)
            val readableDateRange = TimeDateConverter.getReadableDateRange(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate
            )
            ReceiptAnalyticsCategoryResponse(readableDateRange.startDate, readableDateRange.endDate, categoryItem.items)
        }
}
