package com.example.photodownloader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigate(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = NavGraph.Splash.route
    ) {
        composable(route = NavGraph.Splash.route) {

        }
    }
}