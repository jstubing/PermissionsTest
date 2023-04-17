package com.example.permissionstest

interface Destination {
    val route: String
}

object HomeScreen : Destination {
    override val route = "home"
}

object RequiredPermissionsScreen : Destination {
    override val route = "required_permissions"
}

object OptionalPermissionsScreen : Destination {
    override val route = "optional_permissions"
}