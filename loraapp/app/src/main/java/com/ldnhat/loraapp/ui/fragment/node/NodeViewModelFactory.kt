package com.ldnhat.loraapp.ui.fragment.node

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NodeViewModelFactory(
    private val application: Application,
    private val accessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NodeViewModel::class.java)) {
            return NodeViewModel(accessToken, application) as T
        }
        throw IllegalArgumentException()
    }
}