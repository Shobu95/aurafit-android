package com.aurafit.app.di

import com.aurafit.app.BuildConfig
import com.aurafit.app.data.network.createHttpClient
import com.aurafit.app.data.providers.PixelCutProvider
import com.aurafit.app.data.repository.PixelCutRepository
import com.aurafit.app.domain.TryOnProvider
import com.aurafit.app.ui.tryon.TryOnViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }

    single<PixelCutProvider> {
        val http = get<HttpClient>()
        PixelCutProvider(http, BuildConfig.PIXELCUT_URL, BuildConfig.PIXELCUT_API_KEY)
    }
}

val repoModule = module {
    single { PixelCutRepository(get(), androidContext()) }
}

val viewModelModule = module {
    viewModel { TryOnViewModel(get()) }
}
