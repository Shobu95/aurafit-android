package com.aurafit.app.data.repository

import android.content.Context
import android.net.Uri
import com.aurafit.app.data.network.uploadToFirebase
import com.aurafit.app.data.providers.PixelCutProvider
import com.aurafit.app.domain.TryOnResult

class PixelCutRepository(
    private val pixelcut: PixelCutProvider,
    private val appContext: Context
) {
    suspend fun tryOn(personUri: Uri, garmentUri: Uri, uid: String): TryOnResult {
        // (Optional) Quick validations to avoid wasting credits
//        validateOrThrow(appContext, personUri)
//        validateOrThrow(appContext, garmentUri)

        val personUrl = uploadToFirebase(personUri, uid)
        val garmentUrl = uploadToFirebase(garmentUri, uid)

        return pixelcut.tryOnWithUrls(
            personUrl = personUrl,
            garmentUrl = garmentUrl,
            garmentMode = "auto",
            preprocessGarment = true,
            removeBackground = false,
            waitForResult = true
        )
    }

//    private fun validateOrThrow(ctx: Context, uri: Uri) {
//        val cr = ctx.contentResolver
//        val type = cr.getType(uri) ?: error("Unknown file type")
//        require(type == "image/jpeg" || type == "image/png") { "Only JPG/PNG supported" }
//    }
}
