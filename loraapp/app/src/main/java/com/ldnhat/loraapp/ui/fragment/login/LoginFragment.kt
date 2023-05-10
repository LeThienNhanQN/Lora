package com.ldnhat.loraapp.ui.fragment.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.databinding.FragmentLoginBinding
import com.ldnhat.loraapp.utils.constants.Constants

class LoginFragment : Fragment() {
    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginViewModel = loginViewModel

        val sharedPref = requireActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)

        var deviceToken = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            deviceToken = it.result.toString()
        }
        val editor : SharedPreferences.Editor = sharedPref.edit()

        loginViewModel.buttonClick.observe(viewLifecycleOwner, {
            if (null != it) {
                if (it) {
                    loginViewModel.signIn(binding.etInputEmail.text.toString(), binding.etInputPassword.text.toString(), deviceToken)
                    loginViewModel.onClickedSuccess()
                }
            }
        })

        loginViewModel.status.observe(viewLifecycleOwner, {
            Log.d("status: ", it.toString())
        })

        loginViewModel.signIn.observe(viewLifecycleOwner, {
            if (null != it) {
                editor.putString(Constants.ACCESS_TOKEN, it.accessToken)
                editor.putString(Constants.USER_ID, it.userId)
                editor.apply()
                this.findNavController().navigate(LoginFragmentDirections.actionLoginNavToHome())
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }
}