package com.patrickpie12345.server.graphql.hooks

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.patrickpie12345.server.graphql.hooks.scalar.InstantScalar
import com.patrickpie12345.server.graphql.hooks.scalar.UUIDScalar
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ScalarSchemaGeneratorHooks : SchemaGeneratorHooks {

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

    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        Instant::class -> graphqlInstantType
        UUID::class -> graphqlUUIDType
        else -> super.willGenerateGraphQLType(type)
    }
}
