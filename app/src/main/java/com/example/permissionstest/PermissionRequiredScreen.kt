package com.example.permissionstest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.permissionstest.data.Permission
import com.example.permissionstest.util.openAppSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * This composable is meant to wrap a screen composable which won't be displayed until the
 * [requiredPermissions] are granted. It will take care of requesting the permissions and ensuring
 * that all are granted before emitting [permissionGrantedContent]. If the user denies one or more
 * permissions, it will emit [permissionDeniedContent].
 *
 * This screen also auto-refreshes permission status. If the user were to go into app settings
 * and change permission settings, then return to this screen, the permission checks would run
 * again.
 *
 * @param requiredPermissions list of permissions that need to be granted before the screen can render.
 * @param permissionGrantedContent composable content which gets emitted when all [requiredPermissions]
 * are granted.
 * @param permissionDeniedContent composable content which gets emitted when one or more [requiredPermissions]
 * are denied. Takes in the first denied permission and whether or not that permission was permanently
 * denied as a parameter. If this composable is not specified, the default behavior will be to show a
 * rationale screen for the first denied permission with a button to re-request the permission or open
 * app settings to enable it.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequiredScreen(
    requiredPermissions: List<String>,
    permissionGrantedContent: @Composable () -> Unit,
    permissionDeniedContent: @Composable ((deniedPermission: String, isPermanentlyDenied: Boolean) -> Unit)? = null
) {
    var firstDeniedPermission: String? by remember { mutableStateOf(null) }

    val permissionStates =
        rememberMultiplePermissionsState(permissions = requiredPermissions) { results ->
            firstDeniedPermission = results.entries.firstOrNull { !it.value }?.key
        }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Relaunching the permission request in onStart ensures that the permission dialog
    // shows after navigating to app settings and switching permission from "don't allow"
    // to "ask every time", then returning to this screen.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionStates.launchMultiplePermissionRequest()
            } else if (event == Lifecycle.Event.ON_STOP) {
                // Prevents permission dialog from rendering on top of the rationale screen when
                // the lifecycle transitions from started to stopped and back to started.
                firstDeniedPermission = null
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (permissionStates.allPermissionsGranted) {
        permissionGrantedContent()
    } else {
        firstDeniedPermission?.let { permission ->
            val permissionState = permissionStates.permissions.first { it.permission == permission }
            val isPermanentlyDenied = !permissionState.status.shouldShowRationale

            permissionDeniedContent?.invoke(permission, isPermanentlyDenied)
                ?: PermissionDeniedContent(permission, isPermanentlyDenied) {
                    if (permissionState.status.shouldShowRationale) {
                        permissionStates.launchMultiplePermissionRequest()
                    } else {
                        context.openAppSettings()
                    }
                }
        }
    }
}

@Composable
private fun PermissionDeniedContent(
    deniedPermission: String,
    isPermanentlyDenied: Boolean,
    onOpenAppSettingsButtonClick: () -> Unit
) {
    val permission = Permission.fromPermission(deniedPermission)

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(vertical = 120.dp, horizontal = 16.dp)) {
            Icon(
                painter = painterResource(permission.drawableResId),
                contentDescription = null
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.permission_required),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(4.dp))
            Text(stringResource(permission.rationaleResId))
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onOpenAppSettingsButtonClick
        ) {
            Text(
                stringResource(
                    if (isPermanentlyDenied) {
                        R.string.open_app_settings
                    } else {
                        R.string.request_permission
                    }
                )
            )
        }
    }
}