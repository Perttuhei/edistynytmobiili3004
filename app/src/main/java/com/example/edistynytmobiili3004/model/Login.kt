package com.example.edistynytmobiili3004.model

import com.google.gson.annotations.SerializedName

data class LoginReqModel(
    val username : String = "",
    val password: String = "",
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

data class AccountRes(
    @SerializedName("auth_user_id")
    val userId: Int = 0,
    @SerializedName("username")
    val username: String = "",
    @SerializedName("auth_role_auth_role_id")
    val roleId: Int = 0
)

data class AccountState(
    val userId: Int = 0,
    val username: String = "",
    val roleId: Int = 0
)

data class LogoutState(
    val loading: Boolean = false,
    val err: String? = null,
    val logoutOk: Boolean = false)