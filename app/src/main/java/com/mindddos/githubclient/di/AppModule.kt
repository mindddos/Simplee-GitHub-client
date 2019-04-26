package com.mindddos.githubclient.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mindddos.githubclient.repository.remote.GitHubApi
import com.mindddos.githubclient.utils.Const
import com.mindddos.githubclient.vm.SearchScreenVM
import com.mindddos.githubclient.vm.UserDetailsVM
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    single { createOkHttpClient() }
    single {
        createGitHubApi(get(), Const.BASE_URL)
    }
    single { SearchScreenVM(get()) }
    single { UserDetailsVM(get()) }

}

fun createOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}

fun createGitHubApi(okHttpClient: OkHttpClient, baseUrl: String): GitHubApi {
    return Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build().create(GitHubApi::class.java)
}
