package com.ldnhat.loraapp.ui.fragment.node

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.common.model.Node
import com.ldnhat.loraapp.enums.ApiStatus

@BindingAdapter("nodeListData")
fun bindNodeRecyclerView(recyclerView: RecyclerView, data: List<Node>?) {
    val adapter = recyclerView.adapter as NodeAdapter
    adapter.submitList(data)
}

@BindingAdapter("nodeApiStatus")
fun bindStatus(statusImageView: ImageView, status : ApiStatus.State?){
    when (status) {
        ApiStatus.State.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.State.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> statusImageView.visibility = View.GONE
    }
}