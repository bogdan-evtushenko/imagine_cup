package com.example.imaginecup

import android.app.Application

class ICApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this
    }

    companion object {

        lateinit var app: ICApp
            private set
    }
}
