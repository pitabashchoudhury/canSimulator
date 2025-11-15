package com.tata.cansimulator.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CurrentRouteLogger(navController: NavHostController) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry.value?.destination?.route

    LaunchedEffect(currentRoute) {
        Log.d("NAVIGATION", "Current Screen = $currentRoute")
    }
}