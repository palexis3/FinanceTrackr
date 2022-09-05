package com.patrickpie12345.storage.images

import com.patrickpie12345.storage.UpsertResult
import java.util.*

interface ImageStorage {
    suspend fun addImage(awsS3Url: String): UpsertResult<UUID>
}
