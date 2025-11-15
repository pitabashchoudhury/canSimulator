package com.tata.cansimulator.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _message = MutableStateFlow("USB Detail Screen")
    val message = _message.asStateFlow()

    fun showDetails( msg:String){
         _message.value=msg;
    }
    init {
        Log.d("DetailVM", " ViewModel created (init)")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("detail view model","details view model is destroyed")
    }
}