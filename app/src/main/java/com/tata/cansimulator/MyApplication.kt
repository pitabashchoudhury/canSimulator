package com.tata.cansimulator

import android.app.Application
import com.tata.cansimulator.receiver.CarStatusReceiverManager
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class MyApplication : Application(){
    @Inject
    lateinit var receiverManager: CarStatusReceiverManager

    override fun onCreate() {
        super.onCreate()

        receiverManager.start()  // start receiver once
    }
}