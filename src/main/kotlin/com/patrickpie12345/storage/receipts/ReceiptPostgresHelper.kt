package com.patrickpie12345.storage.receipts

import org.intellij.lang.annotations.Language

object ReceiptPostgresHelper {

    @Language("PostgreSQL")
    const val receiptReturn = """
        id,
        title,
        price,
        category,
        image_url,
        created_at
    """
}