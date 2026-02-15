package com.librarix

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LibrarixApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}