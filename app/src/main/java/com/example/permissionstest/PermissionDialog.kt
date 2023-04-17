package com.example.permissionstest

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Configurable dialog which displays a rationale for a permission. This dialog is meant to be shown
 * in the following scenarios:
 *
 * - User denies permission from the system permission dialog
 * - User dismisses the system permission dialog via back button or clicking outside of the dialog
 * - User invokes action which requires permission but permission has been permanently denied
 *
 * @param textProvider provides text for the dialog such as title, description, and button text.
 * @param isPermanentlyDenied whether or not the permission has been permanently denied.
 * @param onRequestPermissionClick the click handler for the request permission button.
 * @param onOpenAppSettingsClick the click handler for the open app settings button.
 * @param onDismiss handler which gets invoked when the dialog is dismissed as a result of a dialog
 * button click, pressing the back button, or clicking outside of the dialog.
 * @param modifier the modifier to apply to this dialog.
 */
@Composable
fun PermissionDialog(
    textProvider: PermissionDialogTextProvider,
    isPermanentlyDenied: Boolean,
    onRequestPermissionClick: () -> Unit,
    onOpenAppSettingsClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                text = if (isPermanentlyDenied) {
                    textProvider.openAppSettingsButton
                } else {
                    textProvider.requestPermissionButton
                },
                modifier = Modifier.clickable {
                    if (isPermanentlyDenied) {
                        onOpenAppSettingsClick()
                    } else {
                        onRequestPermissionClick()
                    }
                    onDismiss()
                })
        },
        dismissButton = {
            Text(
                text = textProvider.cancelButton,
                modifier = Modifier.clickable { onDismiss() }
            )
        },
        title = {
            Text(textProvider.title)
        },
        text = {
            Text(textProvider.getDescription(isPermanentlyDenied))
        },
        modifier = modifier
    )
}

abstract class PermissionDialogTextProvider(private val context: Context) {
    open val title = context.getString(R.string.permission_required)
    open val requestPermissionButton = context.getString(R.string.request_permission)
    open val openAppSettingsButton = context.getString(R.string.open_app_settings)
    open val cancelButton = context.getString(R.string.cancel)

    abstract val rationaleResId: Int

    fun getDescription(isPermanentlyDenied: Boolean): String {
        return if (isPermanentlyDenied) {
            context.getString(rationaleResId) + " " + context.getString(R.string.enable_permission_in_app_settings)
        } else {
            context.getString(rationaleResId)
        }
    }

    companion object {
        fun fromPermission(permission: String, context: Context): PermissionDialogTextProvider {
            return when (permission) {
                CAMERA -> CameraPermissionDialogTextProvider(context)
                ACCESS_FINE_LOCATION -> LocationPermissionDialogTextProvider(context)
                POST_NOTIFICATIONS -> NotificationPermissionDialogTextProvider(context)
                in bluetoothPermissions -> BluetoothPermissionDialogTextProvider(context)
                else -> error("Unsupported permission: $permission")
            }
        }
    }
}

class CameraPermissionDialogTextProvider(context: Context) : PermissionDialogTextProvider(context) {
    override val rationaleResId = R.string.camera_permission_rationale
}

class BluetoothPermissionDialogTextProvider(context: Context) :
    PermissionDialogTextProvider(context) {
    override val rationaleResId = R.string.bluetooth_permission_rationale
}

class LocationPermissionDialogTextProvider(context: Context) :
    PermissionDialogTextProvider(context) {
    override val rationaleResId = R.string.location_permission_rationale
}

class NotificationPermissionDialogTextProvider(context: Context) :
    PermissionDialogTextProvider(context) {
    override val rationaleResId = R.string.notification_permission_rationale
}