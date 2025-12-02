package com.tata.cansimulator.ui.home.state

import com.tata.cansimulator.ui.home.model.CarStatus

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: CarStatus) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

