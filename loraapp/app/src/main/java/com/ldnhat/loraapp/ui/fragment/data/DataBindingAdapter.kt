package com.ldnhat.loraapp.ui.fragment.data

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.enums.ApiStatus
import com.ldnhat.loraapp.enums.LedStatus

@BindingAdapter("ledStateBackground")
fun bindStatus(backgroundState: AppCompatButton, status: LedStatus?) {
    when (status) {
        LedStatus.ON -> {
            backgroundState.setBackgroundColor(backgroundState.context.getColor(R.color.colorBrandBlue))
        }
        LedStatus.OFF -> {
            backgroundState.setBackgroundColor(backgroundState.context.getColor(R.color.colorRed))
        }
    }
}

@BindingAdapter("buttonLedStatus")
fun ledStatus(appCompatButton: AppCompatButton, status: ApiStatus.State?) {
    when (status) {
        ApiStatus.State.LOADING -> {
            appCompatButton.isEnabled = false
        }
        ApiStatus.State.DONE -> {
            appCompatButton.isEnabled = true
        }
        else -> appCompatButton.isEnabled = false
    }
}