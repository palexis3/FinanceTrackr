package com.patrickpie12345.storage

interface UpsertResult<T> {
    data class Ok<T>(val result: T): UpsertResult<T>
    data class NotOk<T>(val error: String): UpsertResult<T>
}