package com.tata.cansimulator.ui.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tata.cansimulator.data.repository.CarRepository
import com.tata.cansimulator.ui.home.model.CarStatus
import com.tata.cansimulator.ui.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = repository.observeCarStatus()
        .map { carStatus ->
            HomeUiState.Success(carStatus) as HomeUiState
        }
        .onStart {
            emit(HomeUiState.Loading)
        }
        .catch { e ->
            emit(HomeUiState.Error(e.localizedMessage ?: "Unknown error"))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeUiState.Loading
        )


    override fun onCleared() {
        super.onCleared()
        Log.d("HomeVM", "Home ViewModel Destroyed")
    }
}
