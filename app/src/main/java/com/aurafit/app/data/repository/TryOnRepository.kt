package com.aurafit.app.data.repository

import android.content.Context
import android.net.Uri
import com.aurafit.app.domain.TryOnProvider
import com.aurafit.app.domain.TryOnResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TryOnRepository(
    private val provider: TryOnProvider,
    private val appContext: Context
) {
    suspend fun tryOn(personUri: Uri, garmentUri: Uri): TryOnResult = withContext(Dispatchers.IO) {
        val personBytes = appContext.contentResolver.openInputStream(personUri)!!.use { it.readBytes() }
        val garmentBytes = appContext.contentResolver.openInputStream(garmentUri)!!.use { it.readBytes() }
        provider.tryOn(personBytes, garmentBytes)
    }
}
