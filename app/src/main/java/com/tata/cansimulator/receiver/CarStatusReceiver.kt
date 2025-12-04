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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarStatusReceiverManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    // ----------------------------------------
    // STATEFLOW (Single Source of Truth)
    // ----------------------------------------
    private val _flow = MutableStateFlow(CarStatus())
    val flow: StateFlow<CarStatus> = _flow


    // ----------------------------------------
    // BROADCAST RECEIVER (Private, Anonymous)
    // ----------------------------------------
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {

            val status: CarStatus? =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    intent?.getParcelableExtra("data", CarStatus::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent?.getParcelableExtra("data")
                }

            if (status != null) {
                _flow.value = status
            }
        }
    }


    // ----------------------------------------
    // START RECEIVER (Call once at app startup)
    // ----------------------------------------
    fun start() {
        val filter = IntentFilter("CAR_STATUS")

        // registerReceiver() is deprecated in some cases → ContextCompat is correct
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }


    // ----------------------------------------
    // STOP RECEIVER (Safely unregister)
    // ----------------------------------------
    fun stop() {
        try {
            context.unregisterReceiver(receiver)
        } catch (_: Exception) {
            // Ignore: receiver may not be registered
        }
    }
}
