package com.example.spacexapp.presentation.navigation

sealed class NavRoutes(val route: String) {
    data object Rockets : NavRoutes("rockets")
    data object Info : NavRoutes("info")
}