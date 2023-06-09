package com.example.permissionstest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.permissionstest.ui.theme.PermissionsTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsTestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PermissionsTestApp()
                }
            }
        }
    }
}