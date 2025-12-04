package com.tata.cansimulator.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarStatus(
    var speed: Int = 0,          // km/h
    val fuelLevel: Int = 0,      // percentage
    val rpm: Int = 2000,         // RPM
    val temperature: Int = 0     // °C
) : Parcelable
