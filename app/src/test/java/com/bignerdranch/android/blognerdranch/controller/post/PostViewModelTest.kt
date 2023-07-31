package com.bignerdranch.android.blognerdranch.controller.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.bignerdranch.android.blognerdranch.data.remote.BlogRemoteDataSource
import com.bignerdranch.android.blognerdranch.data.remote.BlogService
import com.bignerdranch.android.blognerdranch.utils.MainCoroutineRule
import com.bignerdranch.android.blognerdranch.utils.enqueueResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PostViewModelTest {

    private lateinit var postViewModel: PostViewModel

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

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        postViewModel = PostViewModel(BlogRemoteDataSource(api))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPost() {
        mockWebServer.enqueueResponse("1.json", 200)

        runTest(UnconfinedTestDispatcher()) {

            postViewModel.uiState.test {
                postViewModel.getPost(1)
                val result = awaitItem()
                assertThat(result.loading).isTrue()

                val result2 = awaitItem()
                assertThat(result2.loading).isFalse()
                assertThat(result2.post?.id).isEqualTo(1)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun formatDate() {

        val dateInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse("2011-12-03T10:15:30Z"))
        val date = LocalDateTime.ofInstant(dateInstant, ZoneId.of(ZoneOffset.UTC.id))
        val text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

    }
}
