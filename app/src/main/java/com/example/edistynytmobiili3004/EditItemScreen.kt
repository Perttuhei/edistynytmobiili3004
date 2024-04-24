package com.example.edistynytmobiili3004

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.viewmodel.CategoryViewModel
import com.example.edistynytmobiili3004.viewmodel.RentalItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(backToItems: () -> Unit, goToItems: (Int) -> Unit) {

    val vm: RentalItemViewModel = viewModel()
    LaunchedEffect(key1 = vm.itemState.value.ok) {
        if(vm.itemState.value.ok) {
            vm.setOk(false)
            goToItems(vm.itemState.value.item.category.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(vm.itemState.value.item.name) }, navigationIcon = {
                IconButton(onClick = { backToItems() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back to categories"
                    )
                }
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                vm.itemState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                vm.itemState.value.err != null -> Text("Virhe: ${vm.itemState.value.err}")
                else -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        OutlinedTextField(
                            value = vm.itemState.value.item.name,
                            onValueChange = {
                                vm.setName(it)
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            vm.editItem()
                            //goToCategories()


                        }) {
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}


