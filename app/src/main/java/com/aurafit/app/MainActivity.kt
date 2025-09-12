package com.aurafit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aurafit.app.ui.theme.AurafitTheme
import com.aurafit.app.ui.tryon.TryOnScreen

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