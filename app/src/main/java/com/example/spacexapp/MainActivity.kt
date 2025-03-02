package com.example.spacexapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spacexapp.presentation.navigation.BottomNavigationBar
import com.example.spacexapp.presentation.navigation.NavRoutes
import com.example.spacexapp.presentation.screens.info.InfoScreen
import com.example.spacexapp.presentation.screens.rocketdetail.RocketDetailScreen
import com.example.spacexapp.presentation.screens.rockets.RocketsScreen
import com.example.spacexapp.presentation.theme.SpaceXAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route
            if (currentRoute == NavRoutes.Rockets.route || currentRoute == NavRoutes.Info.route) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Rockets.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.Rockets.route) {
                RocketsScreen(
                    onRocketClick = { rocketId ->
                        navController.navigate(NavRoutes.RocketDetail.createRoute(rocketId))
                    }
                )
            }
            composable(NavRoutes.Info.route) {
                InfoScreen()
            }
            composable (
                route = NavRoutes.RocketDetail.route,
                arguments = listOf(navArgument("rocketId") { type = NavType.StringType })
            ) { backStackEntry ->
                val rocketId = backStackEntry.arguments?.getString("rocketId") ?: ""
                RocketDetailScreen(
                    rocketId = rocketId,
                    navController = navController
                )
            }
        }
    }
}