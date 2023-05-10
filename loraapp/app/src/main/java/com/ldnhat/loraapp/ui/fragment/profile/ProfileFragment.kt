package com.ldnhat.loraapp.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.databinding.FragmentProfileBinding
import com.ldnhat.loraapp.utils.constants.Constants

class ProfileFragment : Fragment() {
    private val profileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.profileViewModel = profileViewModel

        val sharedPref =
            requireActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)

        profileViewModel.btnLogoutClick.observe(viewLifecycleOwner, {
            if (it) {
                sharedPref.edit().remove(Constants.ACCESS_TOKEN).apply()
                profileViewModel.onLogoutClickComplete()
                this.findNavController().navigate(ProfileFragmentDirections.actionProfileToHome())
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }
}