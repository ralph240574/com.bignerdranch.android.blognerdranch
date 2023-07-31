package com.bignerdranch.ktor.mockserver.routes

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

fun Route.postMetadataRouting() {
    route("/post-metadata") {
        get {
            val posts = javaClass.getResource("/files/post-metadata/all.json")
                ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)
//            delay(4_000)
            call.respondText(posts.readText())
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Bad Request",
                status = HttpStatusCode.BadRequest
            )
            val post = javaClass.getResource("/files/post-metadata/$id.json")
                ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)
            call.respondText(post.readText())
        }
    }
}

fun Application.registerPostMetadataRoutes() {
    routing {
        postMetadataRouting()
    }
}