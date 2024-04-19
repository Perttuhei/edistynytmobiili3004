package com.example.edistynytmobiili3004.model

import com.google.gson.annotations.SerializedName

data class LoginReqModel(
    val username : String ="Perttu",
    val password: String = "salasana",
    val loading: Boolean = false
)

data class LoginResModel(
    val id: Int = 0,
    val accessToken: String = "",
    val username: String = ""
)

data class LoginState(
    val loading: Boolean = false,
    val err: String? = null,
    val username: String = "",
    val password: String = "",
    val loginOk: Boolean = false)

data class AuthReq(
    val username: String = "",
    val password: String = "")

data class AuthRes(
    @SerializedName("access_token")
    val accessToken: String = ""
)

data class LogoutState(
    val loading: Boolean = false,
    val err: String? = null)