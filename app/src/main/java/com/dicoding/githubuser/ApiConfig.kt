package com.dicoding.githubuser

import com.dicoding.githubuser.network.GithubApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): GithubApiService {
//            val loggingInterceptor = if(BuildConfig.DEBUG) {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//            }

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "token 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")
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(GithubApiService::class.java)
        }
    }
}