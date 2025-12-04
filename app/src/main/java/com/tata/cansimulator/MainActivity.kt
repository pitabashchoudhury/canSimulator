package com.tata.cansimulator

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.tata.cansimulator.navigation.AppNavGraph
import com.tata.cansimulator.navigation.CurrentRouteLogger
import com.tata.cansimulator.navigation.LocalNavController
import com.tata.cansimulator.service.CarStatusService
import com.tata.cansimulator.ui.theme.CAN_SimulatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //@RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start your CAN status service ONCE
        val serviceIntent = Intent(this, CarStatusService::class.java)
        //startForegroundService(serviceIntent)
        ContextCompat.startForegroundService(this, serviceIntent)

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


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, CarStatusService::class.java))
    }

}


