package com.bignerdranch.ktor.mockserver

import com.bignerdranch.ktor.mockserver.routes.registerAuthorImageRoutes
import com.bignerdranch.ktor.mockserver.routes.registerPostMetadataRoutes
import com.bignerdranch.ktor.mockserver.routes.registerPostRoutes
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        Netty,
        port = 8106
    ) {
        install(ContentNegotiation) {
            json()
        }

        registerPostMetadataRoutes()
        registerPostRoutes()
        registerAuthorImageRoutes()
    }.start(wait = true)
}