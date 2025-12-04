package com.tata.cansimulator.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tata.cansimulator.core.utils.CarStatusGenerator
import com.tata.cansimulator.ui.home.model.CarStatus
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class CarStatusService : Service() {

    private val timer = Timer()

    @Inject
    lateinit var generator: CarStatusGenerator

    private val CHANNEL_ID = "car_status_channel"
    private val NOTIFICATION_ID = 101

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification("Starting CAN Data..."))

        startSendingCarStatus()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startSendingCarStatus() {
        timer.schedule(object : TimerTask() {
            override fun run() {

                // Generate CAN data
                val status = generator.generate()

                //  Send broadcast for UI
                val intent = Intent("CAR_STATUS")
                intent.putExtra("data", status)
                sendBroadcast(intent)

                //  Update notification with real values
                updateNotification(status)

                Log.d("serviceStatus", "Broadcast + Notification: $status")
            }
        }, 0, 1000)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Car Status Updates",
                NotificationManager.IMPORTANCE_LOW
            )

            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    // 🔥 Builds initial minimal notification
    private fun buildNotification(text: String) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CAN Simulator")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .build()

    // 🔥 Updates notification with CAN data
    private fun updateNotification(status: CarStatus) {

        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val newNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CAN Simulator")
            .setContentText(
                "Fuel: ${status.fuelLevel}% | Speed: ${status.speed} km/h | RPM: ${status.rpm}"
            )
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .build()

        nm.notify(NOTIFICATION_ID, newNotification)
    }
}
