package com.patrickpie12345.graphql.scalar

import graphql.schema.Coercing
import java.util.UUID

object UUIDScalar : Coercing<UUID, String> {

    override fun parseLiteral(input: Any): UUID {
        TODO("Not yet implemented")
    }

    override fun parseValue(input: Any): UUID {
        TODO("Not yet implemented")
    }

    override fun serialize(dataFetcherResult: Any): String {
        TODO("Not yet implemented")
    }
}