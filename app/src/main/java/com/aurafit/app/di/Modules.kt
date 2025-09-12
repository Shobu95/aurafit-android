package com.aurafit.app.di

import com.aurafit.app.BuildConfig
import com.aurafit.app.data.network.createHttpClient
import com.aurafit.app.data.providers.TryOnApiProvider
import com.aurafit.app.data.repository.TryOnRepository
import com.aurafit.app.domain.TryOnProvider
import com.aurafit.app.ui.tryon.TryOnViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }

    single<TryOnProvider> {
        val http = get<HttpClient>()
        TryOnApiProvider(http, BuildConfig.BASE_URL_TRYON, BuildConfig.API_KEY_TRYON)
//        when (BuildConfig.PROVIDER) {
//            "PIXELCUT" -> PixelcutProvider(http, BuildConfig.BASE_URL_PIXELCUT, BuildConfig.API_KEY_PIXELCUT)
//            "NANOBANANA" -> NanoBananaProvider(http, BuildConfig.BASE_URL_NANOBANANA, BuildConfig.API_KEY_NANOBANANA)
//            else -> TryOnApiProvider(http, BuildConfig.BASE_URL_TRYON, BuildConfig.API_KEY_TRYON)
//        }
    }
}

val repoModule = module {
    single { TryOnRepository(get(), androidContext()) }
}

val viewModelModule = module {
    viewModel { TryOnViewModel(get()) }
}
