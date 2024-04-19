package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.AccountDatabase
import com.example.edistynytmobiili3004.AccountEntity
import com.example.edistynytmobiili3004.DbProvider
import com.example.edistynytmobiili3004.Screen
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.model.AuthReq
import com.example.edistynytmobiili3004.model.LoginReqModel
import com.example.edistynytmobiili3004.model.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val db: AccountDatabase = DbProvider.db): ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    fun setUsername(username: String) {
        _loginState.value = _loginState.value.copy(username = username)
    }

    fun setPassword(password: String) {
        _loginState.value = _loginState.value.copy(password = password)
    }

    fun setLogin(ok: Boolean) {
        _loginState.value = _loginState.value.copy(loginOk = ok)
    }

    private suspend fun _waitForLogin() {
        delay(2000)
    }

    fun login() {

        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)
                val res = authService.login(AuthReq(
                    username = _loginState.value.username,
                    password = _loginState.value.password))
                // Log.d("perttu", res.accessToken)
                db.accountDao().addToken(
                    AccountEntity(accessToken = res.accessToken)
                )
                setLogin(true)
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(err = e.toString())
            } finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }
}