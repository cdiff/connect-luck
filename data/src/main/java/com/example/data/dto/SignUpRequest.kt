package com.example.data.dto

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: Number
)
