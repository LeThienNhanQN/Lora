package com.ldnhat.loraapp.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel : ViewModel() {
    private val _nodeButtonClick = MutableLiveData<Boolean>()

    val nodeButtonClick: LiveData<Boolean>
        get() = _nodeButtonClick

    private val _chartButtonClick = MutableLiveData<Boolean>()

    val chartButtonClick: LiveData<Boolean>
        get() = _chartButtonClick

    private val _notificationButtonClick = MutableLiveData<Boolean>()

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun onNodeButtonClick() {
        _nodeButtonClick.value = true
    }

    fun onNodeButtonClickCompleted() {
        _nodeButtonClick.value = false
    }

    fun onChartButtonClick() {
        _chartButtonClick.value = true
    }

    fun onChartButtonClickCompleted() {
        _chartButtonClick.value = false
    }

    fun onNotificationButtonClick() {
        _notificationButtonClick.value = true
    }

    override fun onCleared() {
        super.onCleared()
        println("viewModel cleared")
        viewModelJob.cancel()
    }
}