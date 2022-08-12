package com.patrickpie12345.graphql.scalar

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.util.UUID

object UUIDScalar : Coercing<UUID, String> {

    override fun parseLiteral(input: Any): UUID {
        val uuidString = (input as? StringValue)?.value
        return runCatching {
            UUID.fromString(uuidString)
        }.getOrElse {
            throw CoercingParseLiteralException("Expected valid UUID but was $input")
        }
    }

    override fun parseValue(input: Any): UUID = runCatching {
        UUID.fromString(serialize(input))
    }.getOrElse {
        throw CoercingParseValueException("Expected valid UUID but was $input")
    }

    override fun serialize(dataFetcherResult: Any): String = runCatching {
        dataFetcherResult.toString()
    }.getOrElse {
        throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a String.")
    }
}
