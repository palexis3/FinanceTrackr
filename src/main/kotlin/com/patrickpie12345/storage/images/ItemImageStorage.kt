package com.patrickpie12345.storage.images

import com.patrickpie12345.storage.UpsertResult
import java.util.*

/**
 * `ItemImageStorage` is different from `ImageStorage` where the latter is used to store
 * attributes into the `Images` table. And the former is used so storage concrete
 * implementations representing items like (`product`, `receipt`, etc) can have an image id
 * stored in their respective tables.
 */
interface ItemImageStorage {
    suspend fun addImageId(id: UUID, imageId: UUID): UpsertResult<String>
}
