package com.example.edistynytmobiili3004

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onMenuClick: () -> Unit, goToLogin: () -> Unit){

    val vm: RegisterViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(key1 = vm.registerState.value.err) {
        vm.registerState.value.err?.let {
            Toast.makeText(context, vm.registerState.value.err, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = vm.registerState.value.registerOk ) {
        if (vm.registerState.value.registerOk) {
            goToLogin()
            vm.setRegister(false)
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Register") }, navigationIcon = {
        IconButton(onClick = { onMenuClick() }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
        }
    })
    }) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                vm.registerState.value.loading -> CircularProgressIndicator(modifier = Modifier.align(
                    Alignment.Center))
                else -> Column( modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = vm.registerState.value.username, onValueChange = {username ->
                        vm.setUsername(username)
                    }, placeholder = {
                        Text(text = stringResource(id = R.string.username))
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = vm.registerState.value.password, onValueChange = {password ->
                        vm.setPassword(password)

                    }, placeholder = {
                        Text(text = stringResource(id = R.string.password))
                    }, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        enabled = vm.registerState.value.username != "" && vm.registerState.value.password != "",
                        onClick = {
                            vm.register()
                        }) {
                        Text(text = stringResource(id = R.string.register))
                    }
                }
            }
        }
    }
}