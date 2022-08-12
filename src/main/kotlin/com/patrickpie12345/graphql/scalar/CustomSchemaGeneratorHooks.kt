package com.patrickpie12345.graphql.scalar

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import java.time.Instant
import java.util.*
import kotlin.reflect.KType
class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {

    private val graphqlInstantType: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Instant")
        .description("A type representing DateTime")
        .coercing(InstantScalar)
        .build()

    private val graphqlUUIDType: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("UUID")
        .description("A type representing a formatted java.util.UUID")
        .coercing(UUIDScalar)
        .build()

    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        Instant::class -> graphqlInstantType
        UUID::class -> graphqlUUIDType
        else -> super.willGenerateGraphQLType(type)
    }
}
