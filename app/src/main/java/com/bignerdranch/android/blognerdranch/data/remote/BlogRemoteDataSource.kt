package com.bignerdranch.android.blognerdranch.data.remote


import com.bignerdranch.android.blognerdranch.data.Result
import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class BlogRemoteDataSource(
    private val blogService: BlogService,
) {

    suspend fun getPostMetadata(): Flow<Result<List<PostMetadata>>> =
        flow {
            emit(Result.Loading)
            val res = safeApiCall(
                call = {
                    val response = blogService.getPostMetadata()
                    if (response.isSuccessful) {
                        val postMetadata = response.body()
                        if (postMetadata != null) {
                            return@safeApiCall Result.Success(postMetadata)
                        }
                    }
                    Result.Error(Exception(response.message()))
                })
            when (res) {
                is Result.Error -> throw res.exception
                is Result.Success -> {
                    emit(Result.Success(res.data))
                }

                else -> {}
            }
        }.flowOn(Dispatchers.IO)
            .catch { exception ->
                emit(Result.Error(exception))
            }

    suspend fun getPost(id: Int): Flow<Result<Post>> =

        flow {
            emit(Result.Loading)
            val res = safeApiCall(
                call = {
                    val response = blogService.getPost(id)
                    if (response.isSuccessful) {
                        val post = response.body()
                        if (post != null) {
                            return@safeApiCall Result.Success(post)
                        }
                    }
                    return@safeApiCall Result.Error(Exception(response.message()))
                })
            when (res) {
                is Result.Error -> throw res.exception
                is Result.Success -> {
                    emit(Result.Success(res.data))
                }

                else -> {}
            }
        }.flowOn(Dispatchers.IO)
            .catch { exception ->
                emit(Result.Error(exception))
            }
}