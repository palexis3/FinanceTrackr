package com.patrickpie12345.service.aws

import java.io.File

interface AwsStorageService {
    suspend fun save(file: File): String
}
