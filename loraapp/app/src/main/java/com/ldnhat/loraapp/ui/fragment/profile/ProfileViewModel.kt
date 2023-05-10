package com.ldnhat.loraapp.ui.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ProfileViewModel : ViewModel() {
    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _btnLogoutClick = MutableLiveData<Boolean>()

    val btnLogoutClick : LiveData<Boolean>
        get() = _btnLogoutClick

    init {
        _btnLogoutClick.value = false
    }

    fun onLogoutClick() {
        _btnLogoutClick.value = true
    }

    fun onLogoutClickComplete() {
        _btnLogoutClick.value = false
    }

    override fun onCleared() {
        super.onCleared()
        println("viewModel cleared")
        viewModelJob.cancel()
    }
}