package com.patrickpie12345.service.analytics

import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.models.receipt.*
import com.patrickpie12345.storage.receipts.ReceiptStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReceiptAnalyticsService(
    private val receiptStorage: ReceiptStorage
) {

    suspend fun getCategorySum(request: ReceiptAnalyticsRequest): ReceiptAnalyticsCategoryResponse = withContext(Dispatchers.IO) {
        val beginningDate = TimeDateConverter.getBeginningDate(request.timeToSearch)
        val beginningDateOffsetDateTime = TimeDateConverter.stringToOffsetDateTime(beginningDate)
        val dbRequest = ReceiptAnalyticsCategoryDBRequest(beginningDateOffsetDateTime, request.category)

        val categoryItem = receiptStorage.getCategorySum(dbRequest)
        val dateRange = TimeDateConverter.getDateRange(beginningDate).split("-")
        ReceiptAnalyticsCategoryResponse(startDate = dateRange[0], endDate = dateRange[1], categoryItem.items)
    }
}
