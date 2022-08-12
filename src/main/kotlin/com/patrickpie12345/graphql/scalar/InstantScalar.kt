package com.patrickpie12345.graphql.scalar

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingSerializeException
import java.time.Instant

object InstantScalar : Coercing<Instant, String> {

    private fun parseInstant(value: String): Instant {
        return try {
            Instant.parse(value)
        } catch (e: Exception) {
            throw CoercingSerializeException("Could not parse Date string: $e")
        }
    }

    override fun parseValue(input: Any): Instant = runCatching {
        parseInstant(serialize(input))
    }.getOrElse {
        throw CoercingSerializeException("Could not parse value for input: $input")
    }

    override fun parseLiteral(input: Any): Instant {
        val dateString = (input as? StringValue)?.value
        return runCatching {
            dateString?.let { parseInstant(it) } ?: throw CoercingSerializeException("Could not parse value for input: $input")
        }.getOrElse {
            throw CoercingParseLiteralException("Expected valid Instant literal but exception encountered: $it")
        }
    }

    override fun serialize(dataFetcherResult: Any): String = runCatching {
        dataFetcherResult.toString()
    }.getOrElse {
        throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a String.")
    }
}
