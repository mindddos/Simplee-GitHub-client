package com.mindddos.githubclient.repository.remote

import com.mindddos.githubclient.repository.remote.models.Repository
import com.mindddos.githubclient.repository.remote.models.UserInfo
import com.mindddos.githubclient.repository.remote.models.UsersSearchResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GitHubApi {
    @GET("/search/users")
    fun searchForUser(@Query("q") keyword: String): Deferred<UsersSearchResult>

    @GET("/users/{username}")
    fun getUserInfo(@Path("username") username: String): Deferred<UserInfo>

    @GET("/users/{username}/repos")
    fun getUserRepos(@Path("username") username: String): Deferred<List<Repository>>
}