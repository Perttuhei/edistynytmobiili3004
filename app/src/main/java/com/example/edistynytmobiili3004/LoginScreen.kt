package com.example.edistynytmobiili3004

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(goToCategories: () -> Unit){

    val loginVm: LoginViewModel = viewModel()

    Scaffold(topBar = { TopAppBar(title = { Text("Login") })
    }) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                loginVm.loginState.value.loading -> CircularProgressIndicator(modifier = Modifier.align(
                    Alignment.Center))
                loginVm.loginState.value.err != null -> Text("Virhe : ${loginVm.loginState.value.err}")
                else -> Column( modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = loginVm.loginState.value.username, onValueChange = {username ->
                        loginVm.setUsername(username)
                    }, placeholder = {
                        Text(text = "Username")
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = loginVm.loginState.value.password, onValueChange = {password ->
                        loginVm.setPassword(password)

                    }, placeholder = {
                        Text(text = "Password")
                    }, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        enabled = loginVm.loginState.value.username != "" && loginVm.loginState.value.password != "",
                        onClick = {
                            loginVm.login()
                            goToCategories()
                        }) {
                        Text(text = "Login")

                    }
                }
            }
        }
    }
}