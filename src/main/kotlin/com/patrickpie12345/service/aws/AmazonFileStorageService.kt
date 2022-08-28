package com.patrickpie12345.service.aws

import com.patrickpie12345.Env
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

class AmazonFileStorageService : FileStorageService {

    private var client: S3Client

    private val bucketName = Env.bucketName
    private val region = Env.region
    private val accessKeyId = Env.accessKeyId
    private val secretAccessKey = Env.secretAccessKey

    init {
        val credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)
        val awsRegion = Region.of(region)
        client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(awsRegion)
            .build()
    }

    override suspend fun save(file: File): String =
        withContext(Dispatchers.IO) {
            client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(file.name).build(),
                RequestBody.fromFile(file)
            )
            val request = GetUrlRequest.builder().bucket(bucketName).key(file.name).build()
            client.utilities().getUrl(request).toExternalForm()
        }
}
