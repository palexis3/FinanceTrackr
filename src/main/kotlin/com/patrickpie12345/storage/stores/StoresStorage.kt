package com.patrickpie12345.storage.stores

import com.patrickpie12345.models.store.Store
import com.patrickpie12345.models.store.StoreCategory
import com.patrickpie12345.models.store.StoreCreate
import com.patrickpie12345.storage.UpsertResult
import io.vertx.sqlclient.Row
import java.util.*

fun Row.toStore() = Store(
    id = this.getUUID("id"),
    name = this.getString("name"),
    category = this.get(StoreCategory::class.java, "store_category")
)

interface StoresStorage {
    suspend fun get(id: UUID): Store?
    suspend fun saveStore(store: StoreCreate): UpsertResult<Store>
}
