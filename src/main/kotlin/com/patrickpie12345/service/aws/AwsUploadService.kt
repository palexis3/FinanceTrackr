package com.patrickpie12345.service.aws

import java.io.File

interface AwsUploadService {
    suspend fun upload(file: File): String
}
