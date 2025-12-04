package com.tata.cansimulator.ui.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tata.cansimulator.data.repository.CarRepository
import com.tata.cansimulator.ui.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CarRepository,
) : ViewModel() {

    private  val pp= repository.observeCarStatus().map { it }

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }


    override fun hashCode(): Int {
        var result = _homeUiState.hashCode()
        result = 31 * result + homeUiState.hashCode()
        return result
    }


    init {
        Log.d("HomeVM", " home ViewModel created (init)")
        loadCarStatus()
    }


    private fun loadCarStatus() {
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading;

            try {
                val result = repository.getCarStatus()
                delay(5000)

                _homeUiState.value = HomeUiState.Success(result)

            } catch (e: Exception) {
                _homeUiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("home view model", "home view model is destroyed")
    }
}