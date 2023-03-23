package com.dicoding.githubuser.network

import com.dicoding.githubuser.response.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<SearchUserResponse>
}