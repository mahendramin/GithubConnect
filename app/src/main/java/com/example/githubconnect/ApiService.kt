package com.example.githubconnect

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun searchUser(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollower(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}
