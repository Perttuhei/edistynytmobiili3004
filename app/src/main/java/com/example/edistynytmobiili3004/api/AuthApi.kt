package com.example.edistynytmobiili3004.api

import com.example.edistynytmobiili3004.model.AccountRes
import com.example.edistynytmobiili3004.model.AuthReq
import com.example.edistynytmobiili3004.model.AuthRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private val retrofit = createClient()

val authService = retrofit.create(AuthApi::class.java)
interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body req: AuthReq) : AuthRes

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") bearerToken: String)

    @GET("auth/account")
    suspend fun account(@Header("Authorization") bearerToken: String): AccountRes

    @POST("auth/register")
    suspend fun register(@Body req: AuthReq)
}