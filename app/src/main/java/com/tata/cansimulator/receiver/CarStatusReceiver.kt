package com.tata.cansimulator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.tata.cansimulator.ui.home.model.CarStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject // <-- FIX: Changed from jakarta.inject.Inject
import javax.inject.Singleton // <-- FIX: Changed from jakarta.inject.Singleton

@Singleton
class CarStatusReceiverManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val _flow = MutableStateFlow(CarStatus())
    val flow: StateFlow<CarStatus> = _flow

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            // Use the modern, safer way to get Parcelable extras for API 33+
            val data = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra("data", CarStatus::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent?.getParcelableExtra<CarStatus>("data")
            }
            data?.let {
                _flow.value = it
            }
        }
    }

    fun start() {
        val filter = IntentFilter("CAR_STATUS")
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun stop() {
        // It's good practice to wrap unregisterReceiver in a try-catch block
        // to prevent crashes if the receiver was already unregistered.
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            // Receiver was not registered, ignore.
        }
    }
}
