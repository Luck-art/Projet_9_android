package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.database.DatabaseModule
import com.openclassrooms.realestatemanager.view_models.ViewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(DatabaseModule, ViewModelsModule)
        }
    }
}