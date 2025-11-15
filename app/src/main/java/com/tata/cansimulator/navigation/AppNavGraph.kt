package com.tata.cansimulator.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tata.cansimulator.ui.details.DetailScreen
import com.tata.cansimulator.ui.details.DetailViewModel
import com.tata.cansimulator.ui.home.HomeScreen
import com.tata.cansimulator.ui.home.HomeViewModel

//@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController, startDestination = Destinations.HOME, route = "home_root"
    ) {

        // -------------------------
        // HOME SCREEN
        // -------------------------
        composable(Destinations.HOME) {


            HomeScreen(

            )
        }

        // -------------------------
        // DETAIL SCREEN
        // -------------------------
        composable(Destinations.DETAIL) { backStackEntry ->

            DetailScreen(

            )
        }
    }
}
