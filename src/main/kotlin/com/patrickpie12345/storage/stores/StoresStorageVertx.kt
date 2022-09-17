package com.patrickpie12345.storage.stores

import com.patrickpie12345.models.store.Store
import com.patrickpie12345.models.store.StoreCategory
import com.patrickpie12345.models.store.StoreCreate
import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple

class StoresStorageVertx(private val client: SqlClient) : StoresStorage {

    /**
     * The saveStore method is in charge of creating a new store if there doesn't
     * exist a current store that matches the unique store name. Therefore, we must
     * run a sub-query to check and see if there exists already a row with the same store
     * name since there's a UNIQUE constraint on it.
     */
    override suspend fun saveStore(store: StoreCreate): UpsertResult<Store> =
        fetchRow(
            client = client,
            query = """
                WITH new_row AS (
                    INSERT INTO public.stores (name, category)
                        SELECT $1, $2
                            WHERE NOT EXISTS (SELECT 1 FROM public.stores WHERE name = $1 AND store_category = $2)
                    RETURNING *
                )
                SELECT * FROM new_row
                UNION
                SELECT * FROM public.stores WHERE name = $1 AND store_category = $2;
            """.trimIndent(),
            args = Tuple.of(store.name, store.category)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Could not save store: ${store.name}")
                else -> UpsertResult.Ok(
                    Store(
                        id = row.getUUID("id"),
                        name = row.getString("name"),
                        category = row.get(StoreCategory::class.java, "store_category")
                    )
                )
            }
        }
}
