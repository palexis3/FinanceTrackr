package com.patrickpie12345.storage.images

import com.patrickpie12345.storage.UpsertResult
import com.patrickpie12345.storage.VertxStorageExtension.fetchRow
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.*

class ImagesTablesStorageVertx(private val client: SqlClient) : ImagesTablesStorage {

    override suspend fun addImage(awsS3Url: String): UpsertResult<UUID> =
        fetchRow(
            client = client,
            query = """
                INSERT INTO public.images(aws_s3_url) VALUES ($1) returning id
            """.trimIndent(),
            args = Tuple.of(awsS3Url)
        ).let { row ->
            when (row) {
                null -> UpsertResult.NotOk("Error inserting image with url: $awsS3Url")
                else -> UpsertResult.Ok(row.getUUID("id"))
            }
        }
}
