package com.aurafit.app.ui.tryon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurafit.app.data.network.uploadToFirebase
import com.aurafit.app.data.repository.PixelCutRepository
import com.aurafit.app.domain.TryOnResult
import io.ktor.utils.io.printStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.util.UUID

class TryOnViewModel(
    private val repo: PixelCutRepository
) : ViewModel() {

    data class UiState(
        val person: Uri? = null,
        val garment: Uri? = null,
        val personUrl: String? = null,
        val garmentUrl: String? = null,
        val resultUrl: String? = null,   // ✅ NEW: display directly via Coil
        val loading: Boolean = false,
        val error: String? = null,
        val resultBitmap: Bitmap? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun setPerson(uri: Uri) = _state.update { it.copy(person = uri) }
    fun setGarment(uri: Uri) = _state.update { it.copy(garment = uri) }

    /**
     * 1) Upload person & garment to Firebase Storage (if URLs not already present)
     * 2) Save URLs in state
     * 3) Call Pixelcut through repository with URLs
     * 4) Decode result to Bitmap
     */
    fun tryOn(uid: String = "uid") = viewModelScope.launch {
        val s0 = _state.value
        if (s0.person == null || s0.garment == null) return@launch

        _state.update { it.copy(loading = true, error = null, resultBitmap = null) }

        try {
            // 1 & 2) Ensure we have URLs for both images (upload if missing)
//            val personUrl = s0.personUrl ?: uploadToFirebase(s0.person, uid).also { url ->
//                _state.update { it.copy(personUrl = url) }
//            }
//            val garmentUrl = s0.garmentUrl ?: uploadToFirebase(s0.garment, uid).also { url ->
//                _state.update { it.copy(garmentUrl = url) }
//            }

            // 3) Call Pixelcut try-on via repository (URL-based)
            val res = repo.tryOn(
                personUri = state.value.person!!,
                garmentUri = state.value.garment!!,
                uid = uid
            )

            // 4) Update UI with decoded bitmap (or error)
            _state.update {
                it.copy(
                    loading = false,
                    resultUrl = res.resultUrl ?: res.imageUrl,
                    error = res.error
                )
            }
        } catch (e: Exception) {
            _state.update { it.copy(loading = false, error = e.message) }
            e.printStack()
        }
    }
}
