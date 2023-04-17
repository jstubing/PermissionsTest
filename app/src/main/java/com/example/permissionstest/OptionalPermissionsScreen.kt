package com.example.permissionstest

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.permissionstest.data.Permission
import com.example.permissionstest.data.permissions
import com.example.permissionstest.ui.theme.PermissionsTestTheme
import com.example.permissionstest.util.openAppSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OptionalPermissionsScreen() {
    val context = LocalContext.current
    var permissionDialogToShow: String? by rememberSaveable { mutableStateOf(null) }

    val cameraPermissionState =
        rememberPermissionState(CAMERA) { permissionGranted ->
            if (permissionGranted) {
                // Camera permission granted, execute camera functionality (using a toast for simplicity)
                Toast.makeText(
                    context,
                    context.getString(R.string.executing_camera_functionality),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                permissionDialogToShow = CAMERA
            }
        }

    val blueToothPermissionState =
        rememberMultiplePermissionsState(bluetoothPermissions) { results ->
            if (results.values.all { it }) {
                // Bluetooth permissions granted, execute bluetooth functionality (using a toast for simplicity)
                Toast.makeText(
                    context,
                    context.getString(R.string.executing_bluetooth_functionality),
                    Toast.LENGTH_SHORT
                ).show()
            }

            permissionDialogToShow = results.entries.firstOrNull { !it.value }?.key
        }

    val locationPermissionState =
        rememberPermissionState(ACCESS_FINE_LOCATION) { permissionGranted ->
            if (permissionGranted) {
                // Location permission granted, execute location functionality (using a toast for simplicity)
                Toast.makeText(
                    context,
                    context.getString(R.string.executing_location_functionality),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                permissionDialogToShow = ACCESS_FINE_LOCATION
            }
        }

    val notificationPermissionState =
        rememberPermissionState(POST_NOTIFICATIONS) { permissionGranted ->
            if (permissionGranted) {
                // Notification permission granted, execute notification functionality (using a toast for simplicity)
                Toast.makeText(
                    context,
                    context.getString(R.string.executing_notification_functionality),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                permissionDialogToShow = POST_NOTIFICATIONS
            }
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(permissions) { permission ->
                    PermissionCard(permission, onClick = {
                        when (permission.name) {
                            CAMERA -> cameraPermissionState.launchPermissionRequest()
                            ACCESS_FINE_LOCATION -> locationPermissionState.launchPermissionRequest()
                            POST_NOTIFICATIONS -> notificationPermissionState.launchPermissionRequest()
                            in bluetoothPermissions -> blueToothPermissionState.launchMultiplePermissionRequest()
                            else -> return@PermissionCard
                        }
                    })
                }
            }
        }
    }

    permissionDialogToShow?.let { permission ->
        val textProvider = PermissionDialogTextProvider.fromPermission(permission, context)

        val permissionState = when (permission) {
            CAMERA -> cameraPermissionState
            ACCESS_FINE_LOCATION -> locationPermissionState
            POST_NOTIFICATIONS -> notificationPermissionState
            in bluetoothPermissions -> blueToothPermissionState.permissions.first { it.permission == permission }
            else -> error("Unsupported permission: $permission")
        }

        PermissionDialog(
            textProvider = textProvider,
            isPermanentlyDenied = !permissionState.status.shouldShowRationale,
            onRequestPermissionClick = {
                if (permission in bluetoothPermissions) {
                    blueToothPermissionState.launchMultiplePermissionRequest()
                } else {
                    permissionState.launchPermissionRequest()
                }
            },
            onOpenAppSettingsClick = {
                context.openAppSettings()
            },
            onDismiss = {
                permissionDialogToShow = null
            })
    }
}

@Composable
fun PermissionCard(
    permission: Permission,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = permission.color
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(permission.drawableResId),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview
@Composable
fun OptionalPermissionsScreenPreview() {
    PermissionsTestTheme {
        OptionalPermissionsScreen()
    }
}