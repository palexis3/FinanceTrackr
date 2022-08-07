package com.patrickpie12345.graphql

import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import io.ktor.server.request.*

/**
 * Custom logic for how this example app should create its context given the [ApplicationRequest]
 */
class KtorGraphQLContextFactory : GraphQLContextFactory<GraphQLContext, ApplicationRequest> {

    override suspend fun generateContextMap(request: ApplicationRequest): Map<Any, Any> = mutableMapOf<Any, Any>().also {
        request.headers["my-custom-header"]?.let { customHeader ->
            "customHeader" to customHeader
        }
    }
}