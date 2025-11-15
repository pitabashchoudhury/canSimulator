package com.tata.cansimulator.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tata.cansimulator.ui.details.DetailScreen
import com.tata.cansimulator.ui.details.DetailViewModel
import com.tata.cansimulator.ui.home.HomeScreen
import com.tata.cansimulator.ui.home.HomeViewModel


@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME
    ) {

        composable(Destinations.HOME) {
            val vm = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = vm,
                onNavigateDetail = {
                    navController.navigate(Destinations.DETAIL)
                },

            )
        }

        composable(Destinations.DETAIL) {
            val vm = hiltViewModel<DetailViewModel>()
            DetailScreen(viewModel = vm)
        }
    }
}

