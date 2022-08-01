val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgresql_version: String by project
val koin_version: String by project
val vertx_client_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.flywaydb.flyway") version "9.0.2"
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
            url = "jdbc:postgresql://db:5432/financeTrackr"
            user = "PG_USER"
            password = "PG_PASSWORD"
            schemas = arrayOf("public")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Postgres DB
    implementation("org.postgresql:postgresql:$postgresql_version")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_version")
    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Vert.x SQL Client
    implementation("io.vertx:vertx-pg-client:$vertx_client_version")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertx_client_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}