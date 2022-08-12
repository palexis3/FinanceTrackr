package com.patrickpie12345.extensions

import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlin.reflect.KClass

suspend inline fun <reified T : Any> ApplicationCall.receiveJsonOrNull(): T? = receiveJsonOrNull(T::class)

suspend fun <T : Any> ApplicationCall.receiveJsonOrNull(type: KClass<T>): T? {
    return try {
        receive(type)
    } catch (cause: Throwable) {
        application.log.debug("Conversion failed, null returned", cause)
        null
    }
}
