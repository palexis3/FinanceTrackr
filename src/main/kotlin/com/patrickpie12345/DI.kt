package com.patrickpie12345

import com.patrickpie12345.storage.receipts.ReceiptStorage
import com.patrickpie12345.storage.receipts.ReceiptStorageVertx
import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import org.koin.dsl.module
object DI {
    fun storageModule() = module {
        single {
            PgConnectOptions().apply {
                port = 5432
                host = "localhost"
                database = "financeTrackr"
                user = "postgres"
                password = "postgres"
            }
        }

        single<PoolOptions> { PoolOptions().setMaxSize(5) }

        single<PgPool> {
            val connectOptions = get<PgConnectOptions>()
            val vertx = Vertx.vertx()
            PgPool.pool(vertx, connectOptions, get())
        }

        single<SqlClient> { get<PgPool>() }

        single<ReceiptStorage> { ReceiptStorageVertx(get()) }
    }
}
