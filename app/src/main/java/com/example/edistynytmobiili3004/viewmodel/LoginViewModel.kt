package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.edistynytmobiili3004.AccountDao
import com.example.edistynytmobiili3004.AccountDatabase
import com.example.edistynytmobiili3004.AccountEntity
import com.example.edistynytmobiili3004.DbProvider
import com.example.edistynytmobiili3004.Screen
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.model.AccountRes
import com.example.edistynytmobiili3004.model.AccountState
import com.example.edistynytmobiili3004.model.AuthReq
import com.example.edistynytmobiili3004.model.AuthRes
import com.example.edistynytmobiili3004.model.LoginReqModel
import com.example.edistynytmobiili3004.model.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val db: AccountDatabase = DbProvider.db, val goToCategories: () -> Unit): ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _accountState = mutableStateOf(AccountState())
    val accountState: State<AccountState> = _accountState

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

    init {
        getAccount()
    }

    fun getAccount() {
        // haetaan accesstoken, suoritetaan request auth/account
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)
                val accessToken = db.accountDao().getToken()
                accessToken?.let {
                    val res = authService.account("Bearer $it")
                    if(res.userId > 0) {
                        _accountState.value = _accountState.value.copy(
                            userId = res.userId,
                            username = res.username,
                            roleId = res.roleId

                        )
                        db.accountDao().addAccount(
                            AccountEntity(
                                userId = res.userId,
                                username = res.username,
                                roleId = res.roleId,
                                accessToken = accessToken))
                        goToCategories()
                    }
                    //Log.d("perttu", "getAccount _accountState userId ${accountState.value.userId}")
                }
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(err = e.toString())
            } finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }
    fun login() {

        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)
                val res = authService.login(AuthReq(
                    username = _loginState.value.username,
                    password = _loginState.value.password))
                db.accountDao().addAccount(
                    AccountEntity(accessToken = res.accessToken)
                )
                setLogin(true)
                _waitForLogin()
                val token = db.accountDao().getToken()
                if (token != null) {
                    getAccount()
                }
                //Log.d("perttu", "token ${token}")
            } catch (e: Exception) {
                Log.d("perttu", "virhe kirjautumisessa ${e.message}")
                _loginState.value = _loginState.value.copy(err = e.toString())
            } finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }
}