package com.aurafit.app.ui.tryon

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TryOnScreen(
    viewModel: TryOnViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()

//    var personUri by remember { mutableStateOf<Uri?>(null) }
//    var garmentUri by remember { mutableStateOf<Uri?>(null) }

    var pickerTarget by remember { mutableStateOf<PickerTarget?>(null) }
    val context = LocalContext.current

    // Single gallery picker used for both tiles
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        when (pickerTarget) {
            PickerTarget.Person -> viewModel.setPerson(uri)
            PickerTarget.Garment -> viewModel.setGarment(uri)
            null -> Unit
        }
        pickerTarget = null
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestPermissionAndPick(target: PickerTarget) {
        pickerTarget = target
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(context, permission) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            pickImageLauncher.launch("image/*")
        } else {
            permissionLauncher.launch(permission)
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("AuraFit") }) },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Button(
                    onClick = { viewModel.tryOn() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) { Text("Try Out") }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PreviewTile(
                    label = "Your Picture",
                    imageUri = state.person,
                    onClick = { requestPermissionAndPick(PickerTarget.Person) },
                    modifier = Modifier.weight(1f)
                )
                PreviewTile(
                    label = "Your Garment",
                    imageUri = state.garment,
                    onClick = { requestPermissionAndPick(PickerTarget.Garment) },
                    modifier = Modifier.weight(1f)
                )
            }
            GeneratedImagePreviewBitmap(
                bitmap = state.resultBitmap?.asImageBitmap(),
                isLoading = state.loading,
                modifier = Modifier.fillMaxWidth()
            )

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


private enum class PickerTarget { Person, Garment }

@Composable
private fun PreviewTile(
    label: String,
    imageUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 1.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable { onClick() }
        ) {
            if (imageUri == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Tap to select",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun GeneratedImagePreviewBitmap(
    bitmap: androidx.compose.ui.graphics.ImageBitmap?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Generated Image", style = MaterialTheme.typography.labelLarge)
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 1.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.3f)
        ) {
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                bitmap == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No result yet",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Generated Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


@Composable
fun AsyncImage(
    model: Uri,
    contentDescription: String,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(model)
    val state = painter.state

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize()
        )
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is AsyncImagePainter.State.Error -> {
                Box(
                    Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Failed to load",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            else -> Unit
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TryOnScreen_Preview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        TryOnScreen()
    }
}