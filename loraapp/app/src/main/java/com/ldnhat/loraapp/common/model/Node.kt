package com.ldnhat.loraapp.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Node(
    val id: String,
    val name: String,
    val userId: String
) : Parcelable