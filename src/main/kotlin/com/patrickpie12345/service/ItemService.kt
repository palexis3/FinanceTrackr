package com.patrickpie12345.service

import com.patrickpie12345.service.aws.AwsImageUploadService
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ImagesTablesStorageVertx
import com.patrickpie12345.storage.images.ItemImageStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.*

abstract class ItemService(private val itemImageStorage: ItemImageStorage) : KoinComponent {

    private val awsUploadService: AwsImageUploadService by inject()
    private val imagesTableStorage: ImagesTablesStorageVertx by inject()

    suspend fun addImage(itemId: String, image: File): UpsertResult<String> =
        withContext(Dispatchers.IO) {
            val imageUrl = awsUploadService.upload(image)
            when (val imageIdUpsertResult = imagesTableStorage.addImage(imageUrl)) {
                is UpsertResult.Ok -> itemImageStorage.addImageId(
                    itemId = UUID.fromString(itemId),
                    imageId = imageIdUpsertResult.result
                )
                else -> UpsertResult.NotOk("Error inserting image for receipt: $itemId")
            }
        }
}
