package com.patrickpie12345.service

import com.patrickpie12345.helper.FromNow
import com.patrickpie12345.helper.OffsetDateRange
import com.patrickpie12345.helper.TimeDateConverter
import com.patrickpie12345.helper.TimeToSearch
import com.patrickpie12345.models.Page
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductCreate
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.models.store.StoreCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.products.ProductStorageVertx
import com.patrickpie12345.storage.stores.StoresStorageVertx
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.*
import java.util.*

class ProductService(
    private val productStorage: ProductStorageVertx,
    private val storesStorage: StoresStorageVertx
) : ItemService(productStorage) {

    suspend fun get(id: String): Product? =
        withContext(Dispatchers.IO) {
            when (val product = productStorage.get(UUID.fromString(id))) {
                null -> null
                else -> product
            }
        }

    suspend fun getAll(): Page<Product>? = withContext(Dispatchers.IO) {
        val productPage = productStorage.getAll()
        if (productPage != null && productPage.items.isNotEmpty()) {
            productPage
        } else {
            null
        }
    }

    suspend fun delete(id: String): UpsertResult<String> =
        withContext(Dispatchers.IO) {
            val productId = UUID.fromString(id)
            productStorage.deleteProductToStores(productId)
            productStorage.delete(productId)
        }

    /**
     * When we're updating a product, there's a possibility that we have to update
     * the `quantity` and when we do, we must also include the time interval from
     * to update the expiration date of the new quantity of the product
     */
    suspend fun update(productUpdate: ProductUpdate): UpsertResult<Product> =
        withContext(Dispatchers.IO) {
            val productToStoreUpsert = productUpdate.productExpiration?.let { productExpiration ->
                val expirationOffsetDate = getExpirationOffsetDateRange(
                    productExpiration.expirationFromNow,
                    productExpiration.quantity
                )
                val productExpirationTuple = Tuple.of(
                    productUpdate.id,
                    expirationOffsetDate.startOffsetDate,
                    expirationOffsetDate.endOffsetDate,
                    productExpiration.quantity
                )
                productStorage.updateProductToStore(productExpirationTuple)
            } ?: UpsertResult.Ok("Default for null product expiration parameter")

            when (productToStoreUpsert) {
                is UpsertResult.Ok -> productStorage.update(productUpdate)
                else -> UpsertResult.NotOk("Product could not be updated...")
            }
        }

    /**
     * To save a product, there's a few queries that must be made to save properly:
     *  1. Saving the intrinsic attributes associated with a product first then getting a productId
     *  2. Save the store associated with a product (if need be) with us getting the associated storeId
     *  3. Get the expiration date that is calculated from the `fromNow` attribute attached to a product
     *  4. Create a tuple that will then be used as an insert
     */
    suspend fun create(productCreate: ProductCreate): UpsertResult<Product> =
        withContext(Dispatchers.IO) {
            val productDBCreate = ProductDBCreate(productCreate.name, productCreate.price)
            when (val productUpsertResult = productStorage.create(productDBCreate)) {
                is UpsertResult.Ok -> {
                    val product = productUpsertResult.result
                    val expirationOffsetDateRange = getExpirationOffsetDateRange(
                        productCreate.expirationFromNow,
                        productCreate.quantity
                    )
                    // must add attributes in order of table insertion [product_id, store_id, updated_at, expired_at]
                    val storeId = getStoreId(productCreate.store)
                    val storeProductTuple = Tuple.of(
                        product.id,
                        storeId,
                        expirationOffsetDateRange.startOffsetDate,
                        expirationOffsetDateRange.endOffsetDate,
                        productCreate.quantity
                    )
                    when (productStorage.addProductToStore(storeProductTuple)) {
                        is UpsertResult.Ok -> UpsertResult.Ok(product)
                        else -> UpsertResult.NotOk("Could not save product with product name: ${productCreate.name}")
                    }
                }
                else -> UpsertResult.NotOk("Could not save product with product name: ${productCreate.name}")
            }
        }

    /**
     * `timeDuration` is calculated based on how many products exist and their estimated longevity.
     * Example: Let's say single pasta lasts 1 week, so 2 pastas should last 2 weeks
     */
    private fun getExpirationOffsetDateRange(fromNow: FromNow, quantity: Int): OffsetDateRange {
        val timeDuration = fromNow.numOf * quantity
        val timeToSearch = TimeToSearch(
            fromNow = FromNow(timeDuration, fromNow.timeInterval)
        )
        return TimeDateConverter.getFutureOffsetDateRange(timeToSearch)
    }

    private suspend fun getStoreId(storeCreate: StoreCreate): UUID? {
        return when (val storeUpsertResult = storesStorage.saveStore(storeCreate)) {
            is UpsertResult.Ok -> storeUpsertResult.result.id
            else -> null
        }
    }
}
