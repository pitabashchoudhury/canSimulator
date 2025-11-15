package com.tata.cansimulator.data.repository



import com.tata.cansimulator.core.utils.NetworkChecker
import com.tata.cansimulator.ui.home.model.CarStatus
import javax.inject.Inject


class CarRepositoryImpl @Inject constructor(
//    private val api: CarApi,
//    private val dao: CarDao,
    private val networkChecker: NetworkChecker,
) : CarRepository {

    override suspend fun getCarStatus(): CarStatus {

        return if (networkChecker.isConnected()) {
            // ✔ ONLINE → Fetch from API
//            val response = api.getCarStatus()
//
//            // Save to DB for offline use
//            dao.insertCar(response.toEntity())
//
//            response.toDomain()
            CarStatus(
                speed = 200,
                fuelLevel = 80,
                rpm = 2000,
                temperature = 100
            );

        } else {
            // ✔ OFFLINE → Load from Room DB
//            dao.getCar()?.toDomain() ?: CarStatus()
            CarStatus(
                speed = 0,
                fuelLevel = 0,
                rpm = 2000,
            );
        }


    }
}
