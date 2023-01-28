package com.patrickpie12345.storage.images

import com.patrickpie12345.storage.UpsertResult
import java.util.UUID

interface ImagesTablesStorage {
    suspend fun addImage(awsS3Url: String): UpsertResult<UUID>
    suspend fun getImageUrl(imageId: UUID): String?
}
