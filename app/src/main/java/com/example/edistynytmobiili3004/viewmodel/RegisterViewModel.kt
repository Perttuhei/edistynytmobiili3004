package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.model.AuthReq
import com.example.edistynytmobiili3004.model.RegisterState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel(): ViewModel() {

    private val _registerState = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> = _registerState

    fun setUsername(username: String) {
        _registerState.value = _registerState.value.copy(username = username)
    }

    fun setPassword(password: String) {
        _registerState.value = _registerState.value.copy(password = password)
    }

    fun setRegister(ok: Boolean) {
        _registerState.value = _registerState.value.copy(registerOk = ok)
    }

    private suspend fun _waitForRegister() {
        delay(2000)
    }
    fun register() {

        viewModelScope.launch {
            try {
                _registerState.value = _registerState.value.copy(loading = true)
                val res = authService.register(
                    AuthReq(
                    username = _registerState.value.username,
                    password = _registerState.value.password)
                )
                setRegister(true)
            } catch (e: Exception) {
                Log.d("perttu", "virhe rekisteröinnissä ${e.message}")
                _registerState.value = _registerState.value.copy(err = e.toString())
            } finally {
                _registerState.value = _registerState.value.copy(loading = false)
            }
        }
    }
}