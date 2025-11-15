package com.tata.cansimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.tata.cansimulator.navigation.AppNavGraph
import com.tata.cansimulator.navigation.CurrentRouteLogger
import com.tata.cansimulator.navigation.LocalNavController
import com.tata.cansimulator.ui.theme.CAN_SimulatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            CAN_SimulatorTheme {
                CompositionLocalProvider(LocalNavController provides navController) {
                    AppNavGraph(navController)
                }
            }
            CurrentRouteLogger(navController)
        }
    }
}


