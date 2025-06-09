package com.dungz.drinkreminder.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("Splash")
    object Main: Screen("Main")
    object Home: Screen("Home")
    object Analyze: Screen("Analyze")
    object Setting: Screen("Settings")
    object Setup: Screen("Setup")
}