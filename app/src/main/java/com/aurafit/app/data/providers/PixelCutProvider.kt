package com.aurafit.app.data.providers

import com.aurafit.app.domain.TryOnProvider
import com.aurafit.app.domain.TryOnResult
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PixelCutProvider(
    private val http: io.ktor.client.HttpClient,
    private val baseUrl: String,
    private val apiKey: String
) : TryOnProvider {

    suspend fun tryOnWithUrls(
        personUrl: String,
        garmentUrl: String,
        garmentMode: String = "auto",
        preprocessGarment: Boolean = true,
        removeBackground: Boolean = false,
        waitForResult: Boolean = true
    ): TryOnResult {
        val resp = http.post(baseUrl) {
            header("X-API-KEY", apiKey)
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "person_image_url" to personUrl,
                    "garment_image_url" to garmentUrl,
                    "garment_mode" to garmentMode,
                    "preprocess_garment" to preprocessGarment.toString(),
                    "remove_background" to removeBackground.toString(),
                    "wait_for_result" to waitForResult.toString()
                )
            )
        }
        return resp.body()
    }

    // bytes variant not supported directly for Pixelcut:
    override suspend fun tryOn(personImage: ByteArray, garmentImage: ByteArray): TryOnResult =
        throw UnsupportedOperationException("Use tryOnWithUrls for Pixelcut")
}
