package com.ldnhat.loraapp.ui.fragment.node

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
import com.ldnhat.loraapp.databinding.FragmentNodeBinding
import com.ldnhat.loraapp.utils.constants.Constants

class NodeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNodeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_node, container, false)
        val sharedPref =
            requireActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
        val accessToken: String? = sharedPref.getString(Constants.ACCESS_TOKEN, null)

        val application = requireActivity().application
        val nodeViewModelFactory = NodeViewModelFactory(application, accessToken.toString())
        val nodeViewModel =
            ViewModelProvider(this, nodeViewModelFactory).get(NodeViewModel::class.java)
        binding.nodeViewModel = nodeViewModel

        binding.nodeGrid.adapter = NodeAdapter(NodeAdapter.OnClickListener {
            nodeViewModel.displayNodeDetail(it)
        })

        nodeViewModel.navigatedToSelectedNode.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(NodeFragmentDirections.actionNodeNavToData(it))
            }
        })

        findNavController().saveState()

        binding.lifecycleOwner = this
        return binding.root
    }
}