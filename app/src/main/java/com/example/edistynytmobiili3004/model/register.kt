package com.example.edistynytmobiili3004.model

data class RegisterState(
    val loading: Boolean = false,
    val err: String? = null,
    val username: String = "",
    val password: String = "",
    val registerOk: Boolean = false)