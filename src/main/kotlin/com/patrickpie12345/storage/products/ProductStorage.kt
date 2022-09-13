package com.patrickpie12345.storage.products

import com.patrickpie12345.models.Page
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.Tuple
import java.time.ZoneOffset
import java.util.*

fun Row.toProduct() = Product(
    id = this.getUUID("id"),
    name = this.getString("name"),
    price = this.getFloat("price"),
    imageId = this.getUUID("image_id"),
    createdAt = this.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC)
)

interface ProductStorage {
    suspend fun get(id: UUID): Product?
    suspend fun getAll(): Page<Product>?
    suspend fun delete(id: UUID): UpsertResult<String>
    suspend fun create(productDBCreate: ProductDBCreate): UpsertResult<Product>
    suspend fun update(productUpdate: ProductUpdate): UpsertResult<Product>
    suspend fun addProductToStore(productToStoreTuple: Tuple): UpsertResult<String>
    suspend fun updateProductToStore(updateProductToStoreTuple: Tuple): UpsertResult<String>
    suspend fun deleteProductToStores(productId: UUID): UpsertResult<String>
}
