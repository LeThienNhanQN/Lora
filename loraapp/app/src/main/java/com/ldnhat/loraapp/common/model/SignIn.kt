package com.ldnhat.loraapp.common.model

import com.ldnhat.loraapp.enums.UserEnum


data class SignIn(
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String,
    val userId: String,
    val expired: Long,
    val status: UserEnum.Status
)
