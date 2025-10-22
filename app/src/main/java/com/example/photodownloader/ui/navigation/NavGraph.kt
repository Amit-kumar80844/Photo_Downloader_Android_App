package com.example.photodownloader.ui.navigation

sealed class NavGraph(
    val route: String
) {
/*    data object Splash : NavGraph("Splash")*/
    data object ImageSearch : NavGraph("HomeScreen")
}