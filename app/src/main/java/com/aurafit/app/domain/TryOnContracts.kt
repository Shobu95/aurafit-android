package com.aurafit.app.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TryOnResult(
    @SerialName("result_url") val resultUrl: String? = null,
    val imageBase64: String? = null,
    val imageUrl: String? = null,
    val error: String? = null
)

interface TryOnProvider {
    /**
     * Returns the composite image (base64 or URL) or an error.
     * Inputs are raw bytes for easy reuse in KMP.
     */
    suspend fun tryOn(personImage: ByteArray, garmentImage: ByteArray): TryOnResult
}
