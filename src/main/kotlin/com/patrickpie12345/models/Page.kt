package com.patrickpie12345.models

data class Page<T>(
    val items: List<T>,
    val total: Int
)
