package com.example.githubconnect

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("location")
    val location: String? = null
)
