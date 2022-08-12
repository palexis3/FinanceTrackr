package com.patrickpie12345.graphql

import com.expediagroup.graphql.server.execution.GraphQLRequestParser
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.request.*
import java.io.IOException

/**
 * Custom logic that enables Ktor to parse incoming [ApplicationRequest] into the [GraphQLServerRequest]
 */
class KtorGraphQLRequestParser(
    private val mapper: ObjectMapper
) : GraphQLRequestParser<ApplicationRequest> {
    override suspend fun parseRequest(request: ApplicationRequest): GraphQLServerRequest? = try {
        val newRequest = request.call.receiveText()
        mapper.readValue(newRequest, GraphQLServerRequest::class.java)
    } catch (e: IOException) {
        throw IOException("Unable to parse GraphQL payload with the exception: $e")
    }
}
