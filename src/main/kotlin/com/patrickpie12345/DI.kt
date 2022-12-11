package com.patrickpie12345

import com.patrickpie12345.service.ProductService
import com.patrickpie12345.service.ReceiptService
import com.patrickpie12345.service.analytics.ProductsAnalyticsService
import com.patrickpie12345.service.analytics.ReceiptAnalyticsService
import com.patrickpie12345.service.aws.AwsImageUploadService
import com.patrickpie12345.service.aws.AwsUploadService
import com.patrickpie12345.storage.images.ImagesTablesStorage
import com.patrickpie12345.storage.images.ImagesTablesStorageVertx
import com.patrickpie12345.storage.products.ProductStorage
import com.patrickpie12345.storage.products.ProductStorageVertx
import com.patrickpie12345.storage.receipts.ReceiptStorage
import com.patrickpie12345.storage.receipts.ReceiptStorageVertx
import com.patrickpie12345.storage.stores.StoresStorage
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
                port = System.getenv("DATABASE_PORT").toInt()
                host = System.getenv("DATABASE_HOST")
                database = System.getenv("DATABASE_NAME")
                user = System.getenv("DATABASE_USERNAME")
                password = System.getenv("DATABASE_PASSWORD")
            }

            //  Uncomment for testing since can't get environment variables
//            PgConnectOptions().apply  {
//                user="postgres"
//                password="postgres"
//                database="financeTrackr"
//                host="localhost"
//                port=5432
//            }
        }

        single<PoolOptions> { PoolOptions().setMaxSize(5) }

        single<PgPool> {
            val connectOptions = get<PgConnectOptions>()
            val vertx = Vertx.vertx()
            PgPool.pool(vertx, connectOptions, get())
        }

        single<SqlClient> { get<PgPool>() }

        single<ReceiptStorage> { ReceiptStorageVertx(get()) }
        single<StoresStorage> { StoresStorageVertx(get()) }
        single<ProductStorage> { ProductStorageVertx(get()) }
        single<ImagesTablesStorage> { ImagesTablesStorageVertx(get()) }

        single<AwsUploadService> { AwsImageUploadService() }
        single { ReceiptService(get(), get()) }
        single { ProductService(get(), get()) }

        single { ReceiptAnalyticsService(get()) }
        single { ProductsAnalyticsService(get(), get()) }
    }
}
