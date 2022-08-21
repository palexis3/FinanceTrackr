package com.patrickpie12345

data class Page<T>(
    val items: List<T>,
    val total: Int
)
