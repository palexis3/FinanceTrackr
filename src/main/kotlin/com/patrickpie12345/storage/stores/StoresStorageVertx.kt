package com.patrickpie12345.storage.stores

import com.patrickpie12345.models.store.Store
import com.patrickpie12345.models.store.StoreCategory
import com.patrickpie12345.models.store.StoreCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple

class StoresStorageVertx(private val client: SqlClient) : StoresStorage {

    override suspend fun saveStore(store: StoreCreate): UpsertResult<Store> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.stores (name, category) VALUES ($1, $2) RETURNING *
            """.trimIndent(),
            args = Tuple.of(store.name, store.category)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not save store: ${store.name}")
                else -> UpsertResult.Ok(
                    Store(
                        id = row.getUUID("id"),
                        name = row.getString("name"),
                        category = row.get(StoreCategory::class.java, "category")
                    )
                )
            }
        }
}
