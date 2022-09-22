package com.patrickpie12345

import io.github.cdimascio.dotenv.dotenv

object Env {

    private val env = dotenv { ignoreIfMissing = true }

    val region: String = env["AWS_REGION"]
    val bucketName: String = env["AWS_BUCKET_NAME"]
    val accessKeyId: String = env["AWS_ACCESS_KEY_ID"]
    val secretAccessKey: String = env["AWS_SECRET_ACCESS_KEY"]

    val serverPort: Int = env["SERVER_PORT"]?.toInt() ?: 3000
    val databasePort: Int = env["DATABASE_PORT"]?.toInt() ?: 5432
    val databaseHost: String = env["DATABASE_HOST"]
    val databaseUserName: String = env["DATABASE_USERNAME"]
    val databasePassword: String = env["DATABASE_PASSWORD"]
}
