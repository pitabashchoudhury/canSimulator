package com.tata.cansimulator.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _text = MutableStateFlow("Home Screen Loaded")
    val text = _text.asStateFlow()

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
    fun updateText(data:String){
        _text.value = data

    }

    override fun hashCode(): Int {
        var result = _text.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}