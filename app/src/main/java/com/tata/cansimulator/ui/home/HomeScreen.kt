package com.tata.cansimulator.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateDetail: () -> Unit,

) {
    val text = viewModel.text.collectAsState()

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text.value)

        Button(onClick = onNavigateDetail) {
            Text("Go To Detail")
        }
        Button(onClick = {
            viewModel.updateText("I like you");
        }) {
            Text("Update")
        }
    }
}
