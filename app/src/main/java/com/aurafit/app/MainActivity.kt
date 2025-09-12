package com.aurafit.app

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.aurafit.app.ui.requestPermissionAndPick
import com.aurafit.app.ui.theme.AurafitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AurafitTheme {
                TryOnScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TryOnScreen() {

    var personUri by remember { mutableStateOf<Uri?>(null) }
    var garmentUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Content picker
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (pickerTarget == PickerTarget.Person) personUri = it
            else garmentUri = it
        }
    }

    // Permission request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) pickerLauncher.launch("image/*")
        else Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("AuraFit") }) },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Button(
                    onClick = { /* TODO: Trigger try-on call */ },
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
                    imageUri = personUri,
                    onClick = {
                        pickerTarget = PickerTarget.Person
                        requestPermissionAndPick(context, permissionLauncher, pickerLauncher)
                    },
                    modifier = Modifier.weight(1f)
                )
                PreviewTile(
                    label = "Your Garment",
                    imageUri = garmentUri,
                    onClick = {
                        pickerTarget = PickerTarget.Garment
                        requestPermissionAndPick(context, permissionLauncher, pickerLauncher)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            GeneratedImagePreview(
                imageUri = null,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


private enum class PickerTarget { Person, Garment }
private var pickerTarget by mutableStateOf(PickerTarget.Person)

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
fun GeneratedImagePreview(
    imageUri: Uri?, // or Bitmap/ImageBitmap if that’s how you’ll hold it later
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Generated Image", style = MaterialTheme.typography.labelLarge)
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 1.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.3f) // bigger, more rectangular preview
        ) {
            if (imageUri == null) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No result yet",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Generated Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
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
    // Use Coil's rememberAsyncImagePainter under the hood
    val painter = rememberAsyncImagePainter(model)

    // Painter's state to show loading / error states if needed
    val state = painter.state

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale
        )

        when (state) {
            is AsyncImagePainter.State.Loading -> {
                // Simple loading overlay
                Box(
                    Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            is AsyncImagePainter.State.Error -> {
                // Show fallback text or icon
                Box(
                    Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> Unit // Success case — image is already shown
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