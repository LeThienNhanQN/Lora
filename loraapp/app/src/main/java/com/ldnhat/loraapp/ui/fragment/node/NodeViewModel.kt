package com.ldnhat.loraapp.ui.fragment.node

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ldnhat.loraapp.common.model.Node
import com.ldnhat.loraapp.enums.ApiStatus
import com.ldnhat.loraapp.network.NodeApi
import com.ldnhat.loraapp.utils.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NodeViewModel(
    accessToken: String,
    app: Application
) : AndroidViewModel(app) {
    private val _status = MutableLiveData<ApiStatus.State>()

    val status: LiveData<ApiStatus.State>
        get() = _status

    private var _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    private var _node = MutableLiveData<List<Node>>()

    val node: LiveData<List<Node>>
        get() = _node

    private var _navigatedToSelectedNode = MutableLiveData<Node>()

    val navigatedToSelectedNode: LiveData<Node>
        get() = _navigatedToSelectedNode

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        userGetAllNode(Constants.BEARER_TOKEN + accessToken)
    }

    private fun userGetAllNode(accessToken: String) {
        try {
            coroutineScope.launch {
                val getAllNodeDeferred =
                    NodeApi.nodeApiService.userGetAllNodeAsync(accessToken)
                _status.value = ApiStatus.State.LOADING

                val result = getAllNodeDeferred.await()
                _status.value = ApiStatus.State.DONE

                _node.value = result
            }
        } catch (e: Exception) {
            _response.value = "Failure: ${e.message}"
            _status.value = ApiStatus.State.ERROR
            _node.value = ArrayList()
        }
    }

    fun displayNodeDetail(node : Node) {
        _navigatedToSelectedNode.value = node
    }

    fun displayNodeDetailComplete() {
        _navigatedToSelectedNode.value = null
    }

    override fun onCleared() {
        super.onCleared()
        println("viewModel cleared")
        viewModelJob.cancel()
    }
}