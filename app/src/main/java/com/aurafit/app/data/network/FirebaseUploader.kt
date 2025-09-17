package com.aurafit.app.data.network

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

suspend fun uploadToFirebase(uri: Uri, uid: String): String {
    val ref = Firebase.storage.reference
        .child("temp/$uid/${UUID.randomUUID()}.jpg")
    ref.putFile(uri).await()                  // upload
    return ref.downloadUrl.await().toString() // public (authorized) URL
}
