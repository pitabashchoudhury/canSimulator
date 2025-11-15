package com.tata.cansimulator.data.repository
import com.tata.cansimulator.ui.home.model.CarStatus

interface CarRepository {
    suspend fun getCarStatus(): CarStatus
}
