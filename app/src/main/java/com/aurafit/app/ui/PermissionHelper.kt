package com.aurafit.app.ui

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

fun requestPermissionAndPick(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    pickerLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    if (ContextCompat.checkSelfPermission(context, permission) ==
        PackageManager.PERMISSION_GRANTED
    ) {
        pickerLauncher.launch("image/*")
    } else {
        permissionLauncher.launch(permission)
    }
}