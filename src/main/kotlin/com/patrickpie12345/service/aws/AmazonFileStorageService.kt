package com.patrickpie12345.service.aws

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

class AmazonFileStorageService : FileStorageService {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private var client: S3Client

    private val bucketName = System.getenv("AWS_BUCKET_NAME")
    private val region = System.getenv("AWS_REGION")
    private val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
    private val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")

    init {
        log.debug("XXX-bucketName: $bucketName")
        log.debug("XXX-region: $region")
        log.debug("XXX-accessKeyId: $accessKeyId")
        log.debug("XXX-secretAccessKey: $secretAccessKey")

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
