package com.example.spacexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spacexapp.presentation.navigation.BottomNavigationBar
import com.example.spacexapp.presentation.navigation.NavRoutes
import com.example.spacexapp.presentation.screens.info.InfoScreen
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
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Rockets.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.Rockets.route) {
                RocketsScreen()
            }
            composable(NavRoutes.Info.route) {
                InfoScreen()
            }
        }
    }
}