package com.revii.storyapp.data.remote.network

import com.revii.storyapp.BuildConfig
import com.revii.storyapp.utils.AppConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkConfig {

    private var token = ""

    fun setToken(value: String) {
        token = value
    }

    private fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            if(request.header("No-Authentication") == null) {
                if(token.isNotEmpty()) {
                    val finalToken = "Bearer $token"
                    request = request.newBuilder()
                        .addHeader(AppConstants.AUTH_HEADER, finalToken)
                        .build()
                }
            }
            chain.proceed(request)
        }
    }

    fun getApiService(): StoryApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(getRequestInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(StoryApiService::class.java)
    }
}