package com.ldnhat.loraapp.ui.fragment.data

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.common.model.Node
import com.ldnhat.loraapp.databinding.FragmentDataBinding
import com.ldnhat.loraapp.utils.constants.Constants

class DataFragment : Fragment() {
    private val dataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_data, container, false)
        binding.dataViewModel = dataViewModel

        val sharedPref =
            requireActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
        val userId: String? = sharedPref.getString(Constants.USER_ID, null)
        val accessToken: String? = sharedPref.getString(Constants.ACCESS_TOKEN, null)

        val node: Node = DataFragmentArgs.fromBundle(requireArguments()).selectedNode

        val db = Firebase.firestore
        db.collection("data")
            .whereEqualTo("userId", userId)
            .whereEqualTo("nodeId", node.id)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.forEach {
                        dataViewModel.setTemperatureValue(it["temperature"].toString() + Constants.SPACE + it["temperatureMeasure"])
                        dataViewModel.setHumidityValue(it["humidity"].toString() + Constants.SPACE + it["humidityMeasure"])
                        dataViewModel.setRssiValue(it["rssiIntensity"].toString() + Constants.SPACE + it["rssiIntensityMeasure"])
                        dataViewModel.setLightValue(it["lightIntensity"].toString() + Constants.SPACE + it["lightIntensityMeasure"])
                        dataViewModel.setSoilValue(it["soilMoisture"].toString() + Constants.SPACE + it["soilMoistureMeasure"])
                    }
                }
            }

        db.collection("led")
            .whereEqualTo("nodeId", node.id)
            .limit(1)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.forEach {
                        dataViewModel.onLedState(
                            it.data["ledState"].toString()
                        )
                    }
                }
            }

        dataViewModel.btnStateLed.observe(viewLifecycleOwner, {
            if (it && null != it) {
                dataViewModel.onChangeStateLed(node.id, accessToken.toString())
                dataViewModel.selectedStateLedComplete()
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(DataFragmentDirections.actionDataNavToNode())
        }

        binding.lifecycleOwner = this
        return binding.root
    }
}