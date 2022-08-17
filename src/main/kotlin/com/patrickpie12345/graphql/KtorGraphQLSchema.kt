package com.patrickpie12345.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import com.patrickpie12345.graphql.hooks.ScalarSchemaGeneratorHooks
import com.patrickpie12345.graphql.mutations.ReceiptMutation
import com.patrickpie12345.graphql.queries.ReceiptQuery
import graphql.GraphQL

/**
 * This Ktor server loads all the queries and configuration to create the [GraphQL] object
 * needed to handle incoming requests.
 */
private val config = SchemaGeneratorConfig(
    supportedPackages = listOf("com.patrickpie12345.graphql"),
    hooks = ScalarSchemaGeneratorHooks()
)

private val queries = listOf(
    TopLevelObject(ReceiptQuery())
)
private val mutations = listOf(
    TopLevelObject(ReceiptMutation())
)

private val graphQLSchema = toSchema(config, queries, mutations)

fun getGraphQLObject(): GraphQL = GraphQL.newGraphQL(graphQLSchema).build()
