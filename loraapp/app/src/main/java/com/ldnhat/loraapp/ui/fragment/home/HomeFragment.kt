package com.ldnhat.loraapp.ui.fragment.home

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
import com.ldnhat.loraapp.databinding.FragmentHomeBinding
import com.ldnhat.loraapp.utils.constants.Constants

class HomeFragment : Fragment() {

    private val homeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val sharedPref =
            requireActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
        val accessToken: String? = sharedPref.getString(Constants.ACCESS_TOKEN, null)

        if (sharedPref.getString(Constants.ACCESS_TOKEN, null).isNullOrEmpty()) {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeToLogin())
        }

        binding.homeViewModel = homeViewModel

        homeViewModel.nodeButtonClick.observe(viewLifecycleOwner, {
            if (it && null != it) {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeToNode())
                homeViewModel.onNodeButtonClickCompleted()
            }
        })
        binding.homeNavigationBar.imvRightIcon.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeToProfile())
        }

        homeViewModel.chartButtonClick.observe(viewLifecycleOwner, {
            if (null != it) {
                if (it) {
                    this.findNavController().navigate(HomeFragmentDirections.actionHomeToChart())
                    homeViewModel.onChartButtonClickCompleted()
                }
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

}