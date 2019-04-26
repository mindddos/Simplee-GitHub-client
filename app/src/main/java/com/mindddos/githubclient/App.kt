package com.mindddos.githubclient

import android.app.Application
import com.mindddos.githubclient.di.appModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}