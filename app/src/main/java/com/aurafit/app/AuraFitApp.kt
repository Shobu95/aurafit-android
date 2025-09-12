package com.aurafit.app

import android.app.Application
import com.aurafit.app.di.networkModule
import com.aurafit.app.di.repoModule
import com.aurafit.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class AuraFitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin DI
        startKoin {
            // Use info-level logs in debug; drop in release
            androidLogger()
            androidContext(this@AuraFitApp)
            modules(
                networkModule,
                repoModule,
                viewModelModule
            )
        }
    }
}
