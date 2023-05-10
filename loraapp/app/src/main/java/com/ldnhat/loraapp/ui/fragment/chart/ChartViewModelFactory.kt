package com.ldnhat.loraapp.ui.fragment.chart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChartViewModelFactory(
    private val application: Application,
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            return ChartViewModel(accessToken, application) as T
        }
        throw IllegalArgumentException()
    }
}