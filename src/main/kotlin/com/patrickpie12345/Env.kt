package com.patrickpie12345

object Env {

    val region: String = System.getenv("AWS_REGION") ?: ""
    val bucketName: String = System.getenv("AWS_BUCKET_NAME") ?: ""
    val accessKeyId: String = System.getenv("AWS_ACCESS_KEY_ID") ?: ""
    val secretAccessKey: String = System.getenv("AWS_SECRET_ACCESS_KEY") ?: ""

    val serverPort: Int = System.getenv("SERVER_PORT")?.toInt() ?: 8080
    val databasePort: Int = System.getenv("DATABASE_PORT")?.toInt() ?: 5432
    val databaseHost: String = System.getenv("DATABASE_HOST") ?: "localhost"
    val databaseUserName: String = System.getenv("DATABASE_USERNAME") ?: "postgres"
    val databasePassword: String = System.getenv("DATABASE_PASSWORD") ?: "postgres"
    val jdbcDatabaseUrl: String = System.getenv("JDBC_DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/financeTrackr"
}
