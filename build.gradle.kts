val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgresql_version: String by project
val koin_version: String by project
val vertx_client_version: String by project
val graphql_version: String by project
val jackson_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.flywaydb.flyway") version "9.0.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("com.expediagroup.graphql") version "6.1.0"
}

val entryPoint = "com.patrickpie12345.ApplicationKt"
group = "com.patrickpie12345"
version = "0.0.1"

application {
    mainClass.set("com.patrickpie12345.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    tasks {
        flyway {
            url = "jdbc:postgresql://localhost:54333/financeTrackr"
            user = "postgres"
            password = "postgres"
            schemas = arrayOf("public")
        }

        shadowJar {
            manifest {
                attributes(
                    "Main-Class" to entryPoint
                )
            }
        }
    }
}

// tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions {
//        allWarningsAsErrors = true
//        apiVersion = "1.5"
//        languageVersion = "1.5"
//        jvmTarget = "11"
//    }
// }

repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    // Postgres DB
    implementation("org.postgresql:postgresql:$postgresql_version")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_version")

    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Vert.x SQL Client
    implementation("io.vertx:vertx-pg-client:$vertx_client_version")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertx_client_version")

    // ExpediaGroup - GraphQL
    implementation("com.expediagroup:graphql-kotlin-server:$graphql_version")
    compileOnly("com.expediagroup:graphql-kotlin-gradle-plugin:$graphql_version")
    compileOnly("com.expediagroup:graphql-kotlin-hooks-provider:$graphql_version")
    compileOnly("com.expediagroup:graphql-kotlin-sdl-generator:$graphql_version")
    implementation("com.expediagroup:graphql-kotlin-schema-generator:$graphql_version")

    // GraphQL SDL (Schema Definition Language) generation
    graphqlSDL("com.expediagroup:graphql-kotlin-federated-hooks-provider:$graphql_version")

    // Jackson serialization
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}

val graphqlGenerateSDL by tasks.getting(com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateSDLTask::class) {
    packages.set(listOf("com.patrickpie12345.graphql"))
}
