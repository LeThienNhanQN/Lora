package com.ldnhat.loraapp.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.loraapp.common.model.SignIn
import com.ldnhat.loraapp.dto.sign_in.SignInRequestDTO
import com.ldnhat.loraapp.enums.ApiStatus
import com.ldnhat.loraapp.network.LoginApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus.State>()

    val status: LiveData<ApiStatus.State>
        get() = _status

    private var _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    private var _signIn = MutableLiveData<SignIn>()

    val signIn: LiveData<SignIn>
        get() = _signIn

    private val _buttonClick = MutableLiveData<Boolean>()

    val buttonClick: LiveData<Boolean>
        get() = _buttonClick

    private var _email = MutableLiveData<String>()

    val email: LiveData<String>
        get() = _email

    private var _password = MutableLiveData<String>()

    val password: LiveData<String>
        get() = _password

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private fun userSignIn(signInRequestDTO: SignInRequestDTO) {
        try {
            coroutineScope.launch {
                val getSignInDeferred = LoginApi.loginApiService.userSignInAsync(signInRequestDTO)
                _status.value = ApiStatus.State.LOADING

                val result = getSignInDeferred.await()
                _status.value = ApiStatus.State.DONE

                _signIn.value = result.data
            }
        } catch (e: Exception) {
            _response.value = "Failure: ${e.message}"
            _status.value = ApiStatus.State.ERROR
            _signIn.value = null
        }
    }

    fun signIn(email: String, password: String, deviceToken: String) {
        userSignIn(SignInRequestDTO(email, password, deviceToken))
    }

    fun onClicked() {
        _buttonClick.value = true
    }

    fun onClickedSuccess() {
        _buttonClick.value = false
    }

    override fun onCleared() {
        super.onCleared()
        println("viewModel cleared")
        viewModelJob.cancel()
    }
}