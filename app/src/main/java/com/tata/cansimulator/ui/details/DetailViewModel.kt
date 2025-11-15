package com.tata.cansimulator.ui.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _message = MutableStateFlow("USB Detail Screen")
    val message = _message.asStateFlow()
}