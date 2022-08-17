package com.patrickpie12345.graphql.hooks

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.plugin.schema.hooks.SchemaGeneratorHooksProvider

/**
 * CustomSchemaGeneratorHooksProvider is needed for gradle task to generate schemas.
 * It has to have a parameterless constructor because it will be created by reflection
 */
class CustomSchemaGeneratorHooksProvider : SchemaGeneratorHooksProvider {
    override fun hooks(): SchemaGeneratorHooks = ScalarSchemaGeneratorHooks()
}
