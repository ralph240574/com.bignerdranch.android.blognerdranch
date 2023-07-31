package com.bignerdranch.android.blognerdranch.data.remote

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BlogService {

    @GET("post-metadata")
    suspend fun getPostMetadata(): Response<List<PostMetadata>>

    @GET("post/{id}")
    suspend fun getPost(@Path("id") id: Int): Response<Post>
}