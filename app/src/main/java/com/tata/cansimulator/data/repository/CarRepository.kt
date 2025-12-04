package com.tata.cansimulator.data.repository
import com.tata.cansimulator.ui.home.model.CarStatus
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    suspend fun getCarStatus(): CarStatus
    fun observeCarStatus(): Flow<CarStatus>
}
