package com.patrickpie12345.graphql.scalar

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import java.time.Instant
import kotlin.reflect.KType

class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {

    private val graphqlInstantType: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Instant")
        .description("A type representing DateTime")
        .coercing(InstantScalar)
        .build()

    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        Instant::class -> graphqlInstantType
        else -> super.willGenerateGraphQLType(type)
    }
}