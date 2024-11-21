package com.zzunee.libraryexample.model.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zzunee.libraryexample.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private const val HEADER_NAVER_CLIENT_ID = "X-Naver-Client-Id"
    private const val HEADER_NAVER_CLIENT_SECRET = "X-Naver-Client-Secret"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(HEADER_NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_ID)
            .addHeader(HEADER_NAVER_CLIENT_SECRET, BuildConfig.NAVER_CLIENT_SECRET)
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BuildConfig.NAVER_OPEN_API_BASE_URL)
        .build()

    val apiService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}