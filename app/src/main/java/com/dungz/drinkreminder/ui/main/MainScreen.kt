package com.dungz.drinkreminder.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dungz.drinkreminder.activity.MainActivityViewModel
import com.dungz.drinkreminder.navigation.Screen
import com.dungz.drinkreminder.ui.main.home.HomeScreen

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val bottomBarHost = rememberNavController()
    Scaffold { innerpadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            navController = bottomBarHost,
            startDestination = Screen.Home.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen()
            }
            composable(route = Screen.Analyze.route) { }
            composable(route = Screen.Setting.route) { }
        }
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar {

    }
}