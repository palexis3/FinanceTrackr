package com.patrickpie12345.service

import com.patrickpie12345.helper.FromNow
import com.patrickpie12345.helper.OffsetDateRange
import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductCreate
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.models.store.StoreCreate
import com.patrickpie12345.service.aws.AwsStorageService
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ImageStorage
import com.patrickpie12345.storage.products.ProductStorage
import com.patrickpie12345.storage.stores.StoresStorage
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class ProductService(
    private val productStorage: ProductStorage,
    private val storesStorage: StoresStorage,
    private val imageStorage: ImageStorage,
    private val awsStorageService: AwsStorageService
) {

    suspend fun get(id: String): Product? =
        withContext(Dispatchers.IO) {
            when (val product = productStorage.get(UUID.fromString(id))) {
                null -> null
                else -> product
            }
        }

    suspend fun updateProduct(productUpdate: ProductUpdate): UpsertResult<Product> =
        withContext(Dispatchers.IO) {
            productStorage.updateProduct(productUpdate)
        }

    suspend fun addImage(productId: String, image: File): UpsertResult<String> =
        withContext(Dispatchers.IO) {
            val imageUrl = awsStorageService.save(image)
            when (val imageIdUpsertResult = imageStorage.addImage(imageUrl)) {
                is UpsertResult.Ok -> productStorage.addImageId(
                    productId = UUID.fromString(productId),
                    imageId = imageIdUpsertResult.result
                )
                else -> UpsertResult.NotOk("Error inserting image for receipt: $productId")
            }
        }

    /**
     * To save a product, there's a few queries that must be made to save properly:
     *  1. Saving the intrinsic attributes associated with a product first then getting a productId
     *  2. Save the list of stores associated with a product (if need be) with us getting the associated storeIds
     *  3. Get the expiration date that is calculated from the `fromNow` attribute attached to a product
     *  4. Create a list of tuples that will then be used as a batch insert
     */
    suspend fun saveProduct(productCreate: ProductCreate): UpsertResult<Product> =
        withContext(Dispatchers.IO) {
            val productDBCreate = ProductDBCreate(productCreate.name, productCreate.price, productCreate.quantity)
            when (val productUpsertResult = productStorage.saveProduct(productDBCreate)) {
                is UpsertResult.Ok -> {
                    val product = productUpsertResult.result
                    val expirationOffsetDateRange = getExpirationOffsetDateRange(
                        productCreate.expirationFromNow,
                        productCreate.quantity
                    )
                    // must add attributes in order of table insertion [product_id, store_id, updated_at, expired_at]
                    val storeProductTuples = getStoreIds(productCreate.stores).map { storeId ->
                        Tuple.of(
                            product.id,
                            storeId,
                            expirationOffsetDateRange.startOffsetDate,
                            expirationOffsetDateRange.endOffsetDate
                        )
                    }
                    productStorage.addProductToStores(storeProductTuples)
                    UpsertResult.Ok(product)
                }
                else -> UpsertResult.NotOk("Could not save product with product name: ${productCreate.name}")
            }
        }

    /**
     * timeDuration is calculated based on how many products exist and their estimated longevity.
     * Example: Let's say single pasta lasts 1 week, so 2 pastas should last 2 weeks
     */
    private fun getExpirationOffsetDateRange(fromNow: FromNow, quantity: Int): OffsetDateRange {
        val timeDuration = fromNow.numOf * quantity
        val timeToSearch = TimeToSearch(
            fromNow = FromNow(timeDuration, fromNow.timeInterval)
        )
        return TimeDateConverter.getOffsetDateRange(timeToSearch)
    }

    private suspend fun getStoreIds(storeCreates: List<StoreCreate>): List<UUID> =
        withContext(Dispatchers.IO) {
            storeCreates.map { storeCreate ->
                async {
                    when (val storeUpsertResult = storesStorage.saveStore(storeCreate)) {
                        is UpsertResult.Ok -> storeUpsertResult.result.id
                        else -> null
                    }
                }
            }.awaitAll().filterNotNull()
        }
}
