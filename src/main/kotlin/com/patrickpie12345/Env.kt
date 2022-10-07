package com.patrickpie12345

object Env {

    val region: String = System.getenv("AWS_REGION") ?: ""
    val bucketName: String = System.getenv("AWS_BUCKET_NAME") ?: ""
    val accessKeyId: String = System.getenv("AWS_ACCESS_KEY_ID") ?: ""
    val secretAccessKey: String = System.getenv("AWS_SECRET_ACCESS_KEY") ?: ""

    val serverPort: Int = System.getenv("SERVER_PORT")?.toInt() ?: 8080
    val databasePort: Int = System.getenv("DATABASE_PORT")?.toInt() ?: 5432
    val databaseHost: String = System.getenv("DATABASE_HOST")
    val databaseUserName: String = System.getenv("DATABASE_USERNAME")
    val databasePassword: String = System.getenv("DATABASE_PASSWORD")
}
