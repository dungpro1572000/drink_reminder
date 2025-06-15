package com.dungz.drinkreminder.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dungz.drinkreminder.navigation.AppNav
import com.dungz.drinkreminder.ui.theme.DrinkReminderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it){
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
            DrinkReminderTheme {
                AppNav(modifier = Modifier.padding())
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DrinkReminderTheme {
        Greeting("Android")
    }
}