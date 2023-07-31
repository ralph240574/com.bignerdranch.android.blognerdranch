package com.bignerdranch.android.blognerdranch.di

import android.content.Context
import com.bignerdranch.android.blognerdranch.data.remote.BlogRemoteDataSource
import com.bignerdranch.android.blognerdranch.data.remote.BlogService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val baseUrl = "http://10.0.2.2:8106/"

    @Singleton
    @Provides
    fun provideBlogRemoteDataSource(
        blogService: BlogService
    ): BlogRemoteDataSource {
        return BlogRemoteDataSource(
            blogService = blogService,
        )
    }

    @Provides
    @Singleton
    fun provideBlogRemoteService(
        retrofit: Retrofit
    ): BlogService {
        return retrofit.create(BlogService::class.java)
    }


    @Provides
    @Singleton
    fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {

        val cacheSize = 4 * 1024 * 1024
        val cache = Cache(
            File(context.cacheDir, "cache"),
            cacheSize.toLong()
        )
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(cache = cache)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}

