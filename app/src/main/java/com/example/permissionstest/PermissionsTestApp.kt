package com.example.permissionstest

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PermissionsTestApp() {
    val navController = rememberNavController()
    PermissionsTestNavHost(navController = navController)
}