package com.tata.cansimulator.ui.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tata.cansimulator.navigation.Destinations
import com.tata.cansimulator.navigation.LocalNavController
import com.tata.cansimulator.ui.home.HomeViewModel


@Composable
fun DetailScreen() {
    val navController = LocalNavController.current


    val currentEntry = navController.currentBackStackEntryAsState().value

    // Use currentEntry as key — safe because it updates when backstack changes
    val homeBackStackEntry = remember(currentEntry) {
        navController.getBackStackEntry(Destinations.HOME)
    }
    val homeViewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
    val detailVM: DetailViewModel = hiltViewModel();


    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(detailVM.message.collectAsState().value)
        Button(onClick = {

        }) {
            Text("press")
        }
    }
}