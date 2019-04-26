package com.mindddos.githubclient.repository.remote.models

import com.google.gson.annotations.SerializedName

data class UsersSearchResult(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val userItems: List<UserItem>,
    @SerializedName("total_count")
    val totalCount: Int
)

