package com.mindddos.githubclient.repository.remote.models

data class UserWithRepos(val userInfo: UserInfo, val repositories: List<Repository>)