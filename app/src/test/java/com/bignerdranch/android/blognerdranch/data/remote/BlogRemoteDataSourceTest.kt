package com.bignerdranch.android.blognerdranch.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.bignerdranch.android.blognerdranch.data.Result
import com.bignerdranch.android.blognerdranch.utils.enqueueResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class BlogRemoteDataSourceTest {

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.SECONDS)
        .writeTimeout(2, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BlogService::class.java)

    private lateinit var blogRemoteDataSource: BlogRemoteDataSource

    @Before
    fun createService() {
        blogRemoteDataSource = BlogRemoteDataSource(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPost() {
        mockWebServer.enqueueResponse("1.json", 200)

        runTest(UnconfinedTestDispatcher()) {

            blogRemoteDataSource.getPost(1).test {
                assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
                val result = awaitItem() as Result.Success
                assertThat(result.data.id).isEqualTo(1)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun getPostMetadata() {
        mockWebServer.enqueueResponse("all.json", 200)

        runTest(UnconfinedTestDispatcher()) {

            blogRemoteDataSource.getPostMetadata().test {
                assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
                val result = awaitItem() as Result.Success
                assertThat(result.data.size).isEqualTo(6)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

}