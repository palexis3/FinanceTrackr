package com.patrickpie12345.storage.images

import com.patrickpie12345.storage.UpsertResult
import java.util.UUID

/**
 * `ItemImageStorage` is different from `ImagesTablesStorage` where the latter is used to store
 * attributes into the `Images` table. And the former is used so storage concrete
 * implementations representing items like (`product`, `receipt`, etc) can have an image id
 * stored in their respective tables.
 */
interface ItemImageStorage {
    suspend fun addImageId(itemId: UUID, imageId: UUID): UpsertResult<String>
}
