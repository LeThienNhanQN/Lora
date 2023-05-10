package com.ldnhat.loraapp.common.model


data class ApiResponse<T>(
    val code : String,

    val data : T
)
