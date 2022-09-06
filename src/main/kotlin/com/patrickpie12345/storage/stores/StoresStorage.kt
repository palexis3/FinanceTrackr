package com.patrickpie12345.storage.stores

import com.patrickpie12345.models.store.Store
import com.patrickpie12345.models.store.StoreCreate
import com.patrickpie12345.storage.UpsertResult

interface StoresStorage {
    suspend fun saveStore(store: StoreCreate): UpsertResult<Store>
}
