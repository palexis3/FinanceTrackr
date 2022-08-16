package com.patrickpie12345.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
fun Application.configureSerialization() {

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
//            register(ContentType.Application.Json, JacksonConverter())
        }
    }
}
