package com.tata.cansimulator.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun UsbScreen(
   // viewModel: UsbViewModel = hiltViewModel()
  modifier: Modifier
) {
    val ctx = LocalContext.current
//    val state by viewModel.connectionState.collectAsState()
//    val data by viewModel.latestData.collectAsState()
//
//    val statusText = when (state) {
//        is com.example.usbserial.data.UsbRepository.ConnectionState.Connected -> "Connected"
//        is com.example.usbserial.data.UsbRepository.ConnectionState.Disconnected -> "Disconnected"
//        is com.example.usbserial.data.UsbRepository.ConnectionState.PermissionRequested -> "Permission requested"
//        is com.example.usbserial.data.UsbRepository.ConnectionState.PermissionRequired -> "Permission required"
//        is com.example.usbserial.data.UsbRepository.ConnectionState.Error -> "Error: ${(state as com.example.usbserial.data.UsbRepository.ConnectionState.Error).reason}"
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(Color(0xFFF3F4F6)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("USB Serial Monitor", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) { Text("Status: statusText", modifier = Modifier.padding(12.dp)) }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp)
        ) {
            Text(text = "data")
        }

        Spacer(Modifier.height(20.dp))

        Row {
            Button(onClick = {
                // Start service if not started
               // ctx.startForegroundService(Intent(ctx, UsbService::class.java))
                Toast.makeText(ctx, "Service started", Toast.LENGTH_SHORT).show()
            }) {
                Text("Start Service")
            }

            Spacer(Modifier.width(12.dp))

            Button(onClick = {
                // Stop service - send stop intent or call Context.stopService
               // ctx.stopService(Intent(ctx, UsbService::class.java))
                Toast.makeText(ctx, "Service stopped", Toast.LENGTH_SHORT).show()
            }) {
                Text("Stop Service")
            }
        }
    }
}