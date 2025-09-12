package com.aurafit.app.ui.tryon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurafit.app.data.repository.TryOnRepository
import com.aurafit.app.domain.TryOnResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL

class TryOnViewModel(private val repo: TryOnRepository) : ViewModel() {
    data class UiState(
        val person: Uri? = null,
        val garment: Uri? = null,
        val loading: Boolean = false,
        val error: String? = null,
        val resultBitmap: Bitmap? = null
    )
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun setPerson(uri: Uri) = _state.update { it.copy(person = uri) }
    fun setGarment(uri: Uri) = _state.update { it.copy(garment = uri) }

    fun tryOn() = viewModelScope.launch {
        val s = _state.value
        if (s.person == null || s.garment == null) return@launch
        _state.update { it.copy(loading = true, error = null, resultBitmap = null) }
        val res = repo.tryOn(s.person, s.garment)
        _state.update { it.copy(loading = false, resultBitmap = res.toBitmap(), error = res.error) }
    }

    private fun TryOnResult.toBitmap(): Bitmap? {
        imageBase64?.let {
            val bytes = Base64.decode(it, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        imageUrl?.let {
            return try {
                URL(it).openStream().use { s -> BitmapFactory.decodeStream(s) }
            } catch (_: Exception) { null }
        }
        return null
    }
}