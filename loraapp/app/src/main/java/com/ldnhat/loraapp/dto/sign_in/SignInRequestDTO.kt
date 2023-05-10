package com.ldnhat.loraapp.dto.sign_in

data class SignInRequestDTO(
    val email : String,
    val password : String,
    val deviceToken : String
)
