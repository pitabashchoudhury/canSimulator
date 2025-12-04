package com.tata.cansimulator.core.utils

import com.tata.cansimulator.ui.home.model.CarStatus
import javax.inject.Inject
import kotlin.random.Random

class CarStatusGenerator @Inject constructor() {

    fun generate(): CarStatus {
        val random = Random(System.currentTimeMillis())
        return CarStatus(
            speed = random.nextInt(0, 150),
            fuelLevel = random.nextInt(10, 100),
            rpm = random.nextInt(1000, 6000),
            temperature = random.nextInt(60, 110)
        )
    }
}