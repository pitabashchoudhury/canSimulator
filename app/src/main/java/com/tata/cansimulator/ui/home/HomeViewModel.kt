package com.tata.cansimulator.ui.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tata.cansimulator.data.repository.CarRepository
import com.tata.cansimulator.ui.home.model.CarStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _text = MutableStateFlow(CarStatus())
    val text = _text.asStateFlow()

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun updateText(data: String) {
        _text.value = _text.value.copy(
            speed = data.toInt(),
            fuelLevel = data.toInt() - 10,
            rpm = _text.value.rpm - 500,
            temperature = data.toInt() - 13
        )

    }

    override fun hashCode(): Int {
        var result = _text.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }


    init {
        Log.d("HomeVM", " home ViewModel created (init)")
        loadCarStatus()
    }



    private fun loadCarStatus() {
        viewModelScope.launch {
            val result = repository.getCarStatus()
            _text.value = result
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("home view model", "home view model is destroyed")
    }
}