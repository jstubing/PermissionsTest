package com.example.permissionstest

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.permissionstest.util.navigateSingleTopTo

@Composable
fun PermissionsTestNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen.route,
        modifier = modifier
    ) {
        composable(route = HomeScreen.route) {
            HomeScreen(
                onRequiredPermissionsButtonClick = {
                    navController.navigateSingleTopTo(RequiredPermissionsScreen.route)
                },
                onOptionalPermissionsButtonClick = {
                    navController.navigateSingleTopTo(OptionalPermissionsScreen.route)
                }
            )
        }
        composable(route = RequiredPermissionsScreen.route) {
            PermissionRequiredScreen(
                requiredPermissions = listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                permissionGrantedContent = { PermissionsGrantedScreen() }
            )
        }
        composable(route = OptionalPermissionsScreen.route) {
            OptionalPermissionsScreen()
        }
    }
}