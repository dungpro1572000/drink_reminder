package com.dungz.drinkreminder.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.dungz.drinkreminder.navigation.AppNav
import com.dungz.drinkreminder.ui.theme.DrinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            // Permission granted, proceed with the functionality that requires the permission
            Log.d("MainActivity", "Permission granted")
        } else {
            // TODO : show toast
            Log.d("MainActivity", "Permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        setContent {
            DrinkTheme {
                AppNav(modifier = Modifier.padding())
            }
        }
    }
}