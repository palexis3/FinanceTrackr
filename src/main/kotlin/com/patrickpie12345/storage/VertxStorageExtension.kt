package com.patrickpie12345.storage

import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple

object VertxStorageExtension {

    suspend fun fetchRow(client: SqlClient, query: String, args: Tuple): Row? =
        client.preparedQuery(query)
            .execute(args)
            .await()
            .singleOrNull()

    suspend fun fetchRowSet(client: SqlClient, query: String, args: Tuple): RowSet<Row>? =
        client.preparedQuery(query)
            .execute(args)
            .await()

    suspend fun batch(client: SqlClient, query: String, args: List<Tuple>): RowSet<Row> =
        client.preparedQuery(query)
            .executeBatch(args)
            .await()
}
