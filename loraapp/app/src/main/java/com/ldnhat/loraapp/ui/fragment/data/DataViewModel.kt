package com.ldnhat.loraapp.ui.fragment.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.loraapp.enums.ApiStatus
import com.ldnhat.loraapp.enums.LedStatus
import com.ldnhat.loraapp.network.LedApi
import com.ldnhat.loraapp.utils.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DataViewModel : ViewModel() {
    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _temperatureValue = MutableLiveData<String>()

    val temperatureValue: LiveData<String>
        get() = _temperatureValue

    private var _humidityValue = MutableLiveData<String>()

    val humidityValue: LiveData<String>
        get() = _humidityValue

    private var _rssiValue = MutableLiveData<String>()

    val rssiValue: LiveData<String>
        get() = _rssiValue

    private var _lightValue = MutableLiveData<String>()

    val lightValue: LiveData<String>
        get() = _lightValue

    private var _soilValue = MutableLiveData<String>()

    val soilValue: LiveData<String>
        get() = _soilValue

    private var _ledState = MutableLiveData<LedStatus>()

    val ledState: LiveData<LedStatus>
        get() = _ledState

    private var _btnStateLed = MutableLiveData<Boolean>()

    val btnStateLed: LiveData<Boolean>
        get() = _btnStateLed

    private val _status = MutableLiveData<ApiStatus.State>()

    val status: LiveData<ApiStatus.State>
        get() = _status

    init {
        _status.value = ApiStatus.State.DONE
    }

    fun onLedState(ledStatus: String) {
        coroutineScope.launch {
            if (ledStatus == LedStatus.ON.toString()) {
                _ledState.value = LedStatus.ON
            } else {
                _ledState.value = LedStatus.OFF
            }
        }
    }

    private fun userChangeLedState(nodeId: String, accessToken: String) {
        try {
            coroutineScope.launch {
                val userChangeLedStateDeferred =
                    LedApi.ledApiService.userChangeLedStateAsync(nodeId, accessToken)
                _status.value = ApiStatus.State.LOADING

                val result = userChangeLedStateDeferred.await()

                _status.value = ApiStatus.State.DONE
                Log.d("change state result: ", result.data)
            }
        } catch (e: Exception) {
            _status.value = ApiStatus.State.ERROR
            Log.d("Failure: ", e.message.toString())
        }
    }

    fun onChangeStateLed(nodeId: String, accessToken: String) {
        userChangeLedState(nodeId, Constants.BEARER_TOKEN + accessToken)
    }

    fun selectedStateLed() {
        _btnStateLed.value = true
    }

    fun selectedStateLedComplete() {
        _btnStateLed.value = false
    }

    fun setTemperatureValue(temperatureValue: String) {
        _temperatureValue.value = temperatureValue
    }

    fun setHumidityValue(humidityValue: String) {
        _humidityValue.value = humidityValue
    }

    fun setRssiValue(rssiValue: String) {
        _rssiValue.value = rssiValue
    }

    fun setLightValue(lightValue: String) {
        _lightValue.value = lightValue
    }

    fun setSoilValue(soilValue: String) {
        _soilValue.value = soilValue
    }

    override fun onCleared() {
        super.onCleared()
        println("viewModel cleared")
        viewModelJob.cancel()
    }
}