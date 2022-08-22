package com.patrickpie12345.models

/**
 * Wrapper object to send list of items (hence the use of generics) to
 * API or GraphQL servers.
 */
data class Page<T>(
    val items: List<T>,
    val total: Int
)
