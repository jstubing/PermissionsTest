package com.example.permissionstest.data

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import androidx.compose.ui.graphics.Color
import com.example.permissionstest.R
import com.example.permissionstest.ui.theme.Blue
import com.example.permissionstest.ui.theme.Green
import com.example.permissionstest.ui.theme.Orange
import com.example.permissionstest.ui.theme.Red

data class Permission(
    val name: String,
    val drawableResId: Int,
    val rationaleResId: Int,
    val color: Color,
) {

    companion object {
        fun fromPermission(permission: String) = when (permission) {
            CAMERA -> {
                Permission(
                    CAMERA,
                    R.drawable.ic_camera,
                    R.string.camera_permission_rationale,
                    Red
                )
            }

            BLUETOOTH -> {
                Permission(
                    BLUETOOTH_SCAN,
                    R.drawable.ic_bluetooth,
                    R.string.bluetooth_permission_rationale,
                    Blue
                )
            }

            ACCESS_FINE_LOCATION -> {
                Permission(
                    ACCESS_FINE_LOCATION,
                    R.drawable.ic_location,
                    R.string.location_permission_rationale,
                    Green
                )
            }

            POST_NOTIFICATIONS -> {
                Permission(
                    POST_NOTIFICATIONS,
                    R.drawable.ic_notification,
                    R.string.notification_permission_rationale,
                    Orange
                )
            }

            else -> error("Unsupported permission: $permission")
        }
    }
}

val permissions = listOf(
    Permission.fromPermission(CAMERA),
    Permission.fromPermission(BLUETOOTH),
    Permission.fromPermission(ACCESS_FINE_LOCATION),
    Permission.fromPermission(POST_NOTIFICATIONS)
)