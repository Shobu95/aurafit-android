package com.aurafit.app.data.providers

import com.aurafit.app.domain.TryOnProvider
import com.aurafit.app.domain.TryOnResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class TryOnApiProvider(
    private val http: HttpClient,
    private val baseUrl: String,
    private val apiKey: String
) : TryOnProvider {

    override suspend fun tryOn(personImage: ByteArray, garmentImage: ByteArray): TryOnResult {
        val response = http.post(baseUrl) {
            header(HttpHeaders.Authorization, "Bearer $apiKey")
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("person", personImage, Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=person.jpg")
                        })
                        append("garment", garmentImage, Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=garment.jpg")
                        })
                        // append("category", "upper_body") // if API supports
                    }
                )
            )
        }
        return response.body() // expects { imageBase64? imageUrl? error? }
    }
}
