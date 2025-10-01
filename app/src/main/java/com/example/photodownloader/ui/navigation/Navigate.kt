package com.example.photodownloader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.photodownloader.ui.imageSearch.HomeScreen

@Composable
fun Navigate(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = NavGraph.ImageSearch.route
    ) {
        /*composable(route = NavGraph.Splash.route) {
            SplashScreen(navController = navController)
        }*/
        composable(route = NavGraph.Splash.route) {
            HomeScreen(navController = navController)
        }
    }
}