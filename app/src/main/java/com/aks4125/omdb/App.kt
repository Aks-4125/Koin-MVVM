package com.aks4125.omdb

import android.app.Application
import com.aks4125.omdb.di.retrofitModule
import com.aks4125.omdb.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //required
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(retrofitModule,viewModelModule))
        }
    }


}