package com.patrickpie12345.plugins

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import java.text.DateFormat

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            registerModule(JavaTimeModule())
            dateFormat = DateFormat.getDateInstance()
        }
    }
}
