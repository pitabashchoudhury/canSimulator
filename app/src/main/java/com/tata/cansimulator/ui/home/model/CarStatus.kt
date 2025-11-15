package com.tata.cansimulator.ui.home.model

data class CarStatus(
    var speed: Int = 0,          // km/h
    val fuelLevel: Int = 0,      // percentage
    val rpm: Int = 2000,       // percentage
    val temperature: Int = 0,     // °C
)