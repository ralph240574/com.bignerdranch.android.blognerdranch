package com.bignerdranch.android.blognerdranch.controller.list

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
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ListViewModelTest {

    private lateinit var listViewModel: ListViewModel

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
        listViewModel = ListViewModel(BlogRemoteDataSource(api))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPostMedaData() {
        mockWebServer.enqueueResponse("all.json", 200)

        runTest(UnconfinedTestDispatcher()) {

            listViewModel.uiState.test {
                listViewModel.getPostMedaData()
                val result = awaitItem()
                assertThat(result.loading).isTrue()

                val result2 = awaitItem()
                assertThat(result2.loading).isFalse()
                assertThat(result2.postMetadataList.size).isEqualTo(6)
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}
