package com.example.permissionstest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.permissionstest.ui.theme.PermissionsTestTheme

@Composable
fun HomeScreen(
    onRequiredPermissionsButtonClick: () -> Unit,
    onOptionalPermissionsButtonClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Button(onClick = onRequiredPermissionsButtonClick) {
                Text(stringResource(R.string.screen_blocking_permissions))
            }

            Button(onClick = onOptionalPermissionsButtonClick) {
                Text(stringResource(R.string.action_blocking_permissions))
            }
        }
    }
}

@Preview(heightDp = 300, widthDp = 300)
@Composable
fun HomeScreenPreview() {
    PermissionsTestTheme {
        HomeScreen({}, {})
    }
}