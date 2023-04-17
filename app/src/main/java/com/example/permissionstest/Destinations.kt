package com.example.permissionstest

interface Destination {
    val route: String
}

object HomeScreen : Destination {
    override val route = "home"
}

object ScreenBlockingPermissionsScreen : Destination {
    override val route = "screen_blocking_permissions"
}

object ActionBlockingPermissionsScreen : Destination {
    override val route = "action_blocking_permissions"
}