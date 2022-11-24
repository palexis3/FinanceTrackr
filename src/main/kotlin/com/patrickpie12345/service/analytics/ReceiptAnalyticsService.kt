package com.patrickpie12345.service.analytics

import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.models.receipt.ReceiptAnalyticsCategoryDBRequest
import com.patrickpie12345.models.receipt.ReceiptAnalyticsCategoryResponse
import com.patrickpie12345.models.receipt.ReceiptAnalyticsRequest
import com.patrickpie12345.storage.receipts.ReceiptStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReceiptAnalyticsService(private val receiptStorage: ReceiptStorage) {

    suspend fun getCategorySum(request: ReceiptAnalyticsRequest): ReceiptAnalyticsCategoryResponse =
        withContext(Dispatchers.IO) {
            val offsetDateRange = TimeDateConverter.getPastOffsetDateRange(request.timeToSearch)
            val dbRequest = ReceiptAnalyticsCategoryDBRequest(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate,
                request.category
            )

            val storeCategorySum = receiptStorage.getCategorySum(dbRequest)
            val readableDateRange = TimeDateConverter.getReadableDateRange(
                offsetDateRange.startOffsetDate,
                offsetDateRange.endOffsetDate
            )
            ReceiptAnalyticsCategoryResponse(
                readableDateRange.startDate,
                readableDateRange.endDate,
                storeCategorySum.items
            )
        }
}
