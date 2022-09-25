package com.patrickpie12345

import io.github.cdimascio.dotenv.dotenv

object Env {

    private val env = dotenv()

    val region: String = env["AWS_REGION"]
    val bucketName: String = env["AWS_BUCKET_NAME"]
    val accessKeyId: String = env["AWS_ACCESS_KEY_ID"]
    val secretAccessKey: String = env["AWS_SECRET_ACCESS_KEY"]

    val serverPort: Int = env["SERVER_PORT"].toInt()
    val databasePort: Int = env["DATABASE_PORT"].toInt()
    val databaseHost: String = env["DATABASE_HOST"]
    val databaseUserName: String = env["DATABASE_USERNAME"]
    val databasePassword: String = env["DATABASE_PASSWORD"]
}
