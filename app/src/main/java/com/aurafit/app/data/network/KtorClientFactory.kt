package com.aurafit.app.data.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit

fun createHttpClient(): HttpClient = HttpClient(OkHttp) {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("HTTP call", message)
            }
        }
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
            }
        )
    }
    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(120, TimeUnit.SECONDS)
            writeTimeout(120, TimeUnit.SECONDS)
            connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
        }
    }
}
