package com.ldnhat.loraapp.ui.fragment.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ldnhat.loraapp.common.model.Data
import com.ldnhat.loraapp.enums.ApiStatus
import com.ldnhat.loraapp.network.DataApi
import com.ldnhat.loraapp.utils.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChartViewModel(
    accessToken: String,
    app: Application
) : AndroidViewModel(app) {
    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _status = MutableLiveData<ApiStatus.State>()

    val status: LiveData<ApiStatus.State>
        get() = _status

    private var _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    private var _data = MutableLiveData<List<Data>>()

    val data: LiveData<List<Data>>
        get() = _data

    init {
        userGetAllData(Constants.BEARER_TOKEN + accessToken)
    }

    private fun userGetAllData(accessToken: String) {
        try {
            coroutineScope.launch {
                val getAllDataDeferred =
                    DataApi.dataApiService.userGetAllDataAsync(accessToken, null)
                _status.value = ApiStatus.State.LOADING

                val result = getAllDataDeferred.await()
                _status.value = ApiStatus.State.DONE

                _data.value = result
            }
        } catch (e: Exception) {
            _response.value = "Failure: ${e.message}"
            _status.value = ApiStatus.State.ERROR
            _data.value = ArrayList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("viewModel cleared")
        viewModelJob.cancel()
    }
}