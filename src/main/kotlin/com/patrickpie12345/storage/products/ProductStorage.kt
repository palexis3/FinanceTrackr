package com.patrickpie12345.storage.products

import ProductCategoryDBAnalyticsRequest
import ProductCategorySum
import ProductStoreSum
import ProductStoresDBAnalyticsRequest
import com.patrickpie12345.helper.NumberConverter
import com.patrickpie12345.models.Page
import com.patrickpie12345.models.product.Product
import com.patrickpie12345.models.product.ProductDBCreate
import com.patrickpie12345.models.product.ProductToStoreDBCreate
import com.patrickpie12345.models.product.ProductToStoreDBUpdate
import com.patrickpie12345.models.product.ProductUpdate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.images.ItemImageStorage
import io.vertx.sqlclient.Row
import java.time.ZoneOffset
import java.util.UUID

fun Row.toProduct() = Product(
    id = this.getUUID("id"),
    name = this.getString("name"),
    price = NumberConverter.floatToDollarConversion(this.getFloat("price")),
    createdAt = this.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC),
    productCategory = this.getString("product_category"),
).also {
    it.imageUrl = this.getString("aws_s3_url")
}

interface ProductStorage : ItemImageStorage {
    suspend fun get(id: UUID): Product?
    suspend fun getByCategory(productCategory: String): Page<Product>
    suspend fun getByStore(storeId: UUID): Page<Product>
    suspend fun getAll(): Page<Product>?
    suspend fun delete(id: UUID): UpsertResult<String>
    suspend fun create(productDBCreate: ProductDBCreate): UpsertResult<Product>
    suspend fun update(productUpdate: ProductUpdate): UpsertResult<Product>
    suspend fun addProductToStore(productToStoreDBCreate: ProductToStoreDBCreate): UpsertResult<String>
    suspend fun updateProductToStore(productToStoreDBUpdate: ProductToStoreDBUpdate): UpsertResult<String>
    suspend fun deleteProductToStores(productId: UUID): UpsertResult<String>
    suspend fun getCategorySum(productCategoryRequest: ProductCategoryDBAnalyticsRequest): Page<ProductCategorySum>
    suspend fun getStoreSum(productStoreRequest: ProductStoresDBAnalyticsRequest): Page<ProductStoreSum>
}
