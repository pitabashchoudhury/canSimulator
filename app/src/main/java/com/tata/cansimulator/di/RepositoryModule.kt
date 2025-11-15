package com.tata.cansimulator.di



import com.tata.cansimulator.data.repository.CarRepository
import com.tata.cansimulator.data.repository.CarRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarRepository(
        impl: CarRepositoryImpl
    ): CarRepository
}
