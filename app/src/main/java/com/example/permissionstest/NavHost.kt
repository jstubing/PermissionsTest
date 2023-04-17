package com.example.permissionstest

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
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
                    navController.navigateSingleTopTo(ScreenBlockingPermissionsScreen.route)
                },
                onOptionalPermissionsButtonClick = {
                    navController.navigateSingleTopTo(ActionBlockingPermissionsScreen.route)
                }
            )
        }
        composable(route = ScreenBlockingPermissionsScreen.route) {
            PermissionRequiredScreen(
                requiredPermissions = listOf(CAMERA, ACCESS_FINE_LOCATION),
                permissionGrantedContent = { PermissionsGrantedScreen() }
            )
        }
        composable(route = ActionBlockingPermissionsScreen.route) {
            OptionalPermissionsScreen()
        }
    }
}