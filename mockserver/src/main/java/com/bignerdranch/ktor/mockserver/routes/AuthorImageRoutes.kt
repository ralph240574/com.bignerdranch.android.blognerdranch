package com.bignerdranch.ktor.mockserver.routes

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.authorImageRouting() {
    route("/images/authors") {
        get("{author}") {
            val author = call.parameters["author"] ?: return@get call.respondText(
                "Bad Request",
                status = HttpStatusCode.BadRequest
            )
            val image = javaClass.getResourceAsStream("/files/images/authors/$author")
                ?: return@get call.respondText(
                    "Not Found",
                    status = HttpStatusCode.NotFound
                )

            call.respond(
                withContext(Dispatchers.IO) {
                    image.readBytes()
                }
            )
        }
    }
}

fun Application.registerAuthorImageRoutes() {
    routing {
        authorImageRouting()
    }
}