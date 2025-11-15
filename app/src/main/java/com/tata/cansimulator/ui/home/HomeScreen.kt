package com.tata.cansimulator.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tata.cansimulator.navigation.Destinations
import com.tata.cansimulator.navigation.LocalNavController
import com.tata.cansimulator.ui.home.model.CarStatus

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current;
    val homeVM = hiltViewModel<HomeViewModel>()
    val carData: CarStatus = homeVM.text.collectAsState().value;


    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.Transparent)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Car Dashboard",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
            item {
                DashboardCard(
                    title = "Fuel Level",
                    value = "${carData.fuelLevel.toString()} %",
                    onClick = {
                        navController.navigate(Destinations.DETAIL)
                    })
            }
            item {
                DashboardCard(
                    title = "Speed",
                    value = "${carData.speed.toString()} km/h",
                    onClick = {
                        navController.navigate(Destinations.DETAIL)
                    })
            }
            item {
                DashboardCard(
                    title = "Engine Temp",
                    value = "${carData.temperature.toString()} °C",
                    onClick = {

                        navController.navigate(Destinations.DETAIL)
                    })
            }
            item {
                DashboardCard(title = "RPM", value = carData.rpm.toString(), onClick = {

                    navController.navigate(Destinations.DETAIL)
                })
            }
        }

//        DashboardCard(
//            title = "Car Speed", value = "68 km/h", onClick = {
//
//                navController.navigate(Destinations.DETAIL)
//            })
//
//
//        DashboardCard(
//            title = "Engine Temperature", value = "89°C", onClick = {
//
//                navController.navigate(Destinations.DETAIL)
//            })
    }
}


@Composable
fun DashboardCard(title: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
