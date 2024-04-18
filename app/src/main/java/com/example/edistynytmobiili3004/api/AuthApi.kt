package com.example.edistynytmobiili3004.api

import com.example.edistynytmobiili3004.model.AuthReq
import com.example.edistynytmobiili3004.model.AuthRes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private val retrofit = createClient()

val authService = retrofit.create(AuthApi::class.java)
interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body req: AuthReq) : AuthRes
}