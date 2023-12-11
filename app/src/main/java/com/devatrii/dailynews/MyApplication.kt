package com.devatrii.dailynews

import android.app.Application
import com.devatrii.dailynews.repository.MainRepository

class MyApplication:Application() {
    lateinit var repository: MainRepository
    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {

    }
}