package com.patrickpie12345.server.graphql

import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.execution.GraphQLServer
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.request.*

/**
 * Helper method that creates the [GraphQLServer] object that
 * can handle requests.
 */
class KtorGraphQLServer(
    requestParser: KtorGraphQLRequestParser,
    contextFactory: KtorGraphQLContextFactory,
    requestHandler: GraphQLRequestHandler
) : GraphQLServer<ApplicationRequest>(requestParser, contextFactory, requestHandler)

fun getGraphQLServer(mapper: ObjectMapper): KtorGraphQLServer {
    val contextFactory = KtorGraphQLContextFactory()
    val requestParser = KtorGraphQLRequestParser(mapper)
    val graphQL = getGraphQLObject()
    val requestHandler = GraphQLRequestHandler(graphQL)

    return KtorGraphQLServer(requestParser, contextFactory, requestHandler)
}
