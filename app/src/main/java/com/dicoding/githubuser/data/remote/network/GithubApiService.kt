package com.dicoding.githubuser.data.remote.network

import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.data.remote.response.SearchUserResponse
import com.dicoding.githubuser.data.remote.response.UserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>
}