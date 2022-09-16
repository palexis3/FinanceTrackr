package com.patrickpie12345

import com.patrickpie12345.service.ProductService
import com.patrickpie12345.service.ReceiptService
import com.patrickpie12345.service.analytics.ProductsAnalyticsService
import com.patrickpie12345.service.analytics.ReceiptAnalyticsService
import com.patrickpie12345.service.aws.AwsImageUploadService
import com.patrickpie12345.storage.images.ImagesTablesStorageVertx
import com.patrickpie12345.storage.products.ProductStorageVertx
import com.patrickpie12345.storage.receipts.ReceiptStorageVertx
import com.patrickpie12345.storage.stores.StoresStorageVertx
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

        single { ReceiptStorageVertx(get()) }
        single { StoresStorageVertx(get()) }
        single { ProductStorageVertx(get()) }
        single { ImagesTablesStorageVertx(get()) }

        single { AwsImageUploadService() }
        single { ReceiptService(get(), get()) }
        single { ProductService(get(), get()) }

        single { ReceiptAnalyticsService(get()) }
        single { ProductsAnalyticsService(get()) }
    }
}
