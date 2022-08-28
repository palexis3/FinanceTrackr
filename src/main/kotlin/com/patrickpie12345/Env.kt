package com.patrickpie12345

import io.github.cdimascio.dotenv.dotenv

object Env {

    private val DOTENV = dotenv()

    val region: String = DOTENV.get("AWS_REGION")
    val bucketName: String = DOTENV.get("AWS_BUCKET_NAME")
    val accessKeyId: String = DOTENV.get("AWS_ACCESS_KEY_ID")
    val secretAccessKey: String = DOTENV.get("AWS_SECRET_ACCESS_KEY")
}
