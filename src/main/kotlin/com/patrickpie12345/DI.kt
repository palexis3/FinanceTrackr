package com.patrickpie12345

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
                host = "db"
                database = "financeTrackr"
                user = "PG_USER"
                password = "PG_PASSWORD"
            }
        }

        single<PoolOptions> { PoolOptions().setMaxSize(5) }

        single<PgPool> {
            val connectOptions = get<PgConnectOptions>()
            val vertx = Vertx.vertx()
            PgPool.pool(vertx, connectOptions, get())
        }

        single<SqlClient> { get<PgPool>() }
    }
}