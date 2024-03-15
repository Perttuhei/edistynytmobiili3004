package com.example.edistynytmobiili3004

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.viewmodel.CategoriesViewModel
import com.example.edistynytmobiili3004.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryScreen(goToGategories: () -> Unit, backToCategories: () -> Unit) {

    val vm: CategoryViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = vm.categoryState.value.item.name)
            }, navigationIcon = { IconButton(onClick = { backToCategories() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back to categories")
            }})
        }
    ) {


        LaunchedEffect(key1 = vm.categoryState.value.ok) {
            if(vm.categoryState.value.ok) {
                goToGategories()
                vm.setDone(false)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                vm.categoryState.value.loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center)
                )

                vm.categoryState.value.err != null -> Text(text = "Virhe: ${vm.categoryState.value.err}")
                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = vm.categoryState.value.item.name,
                        onValueChange = { name ->
                            vm.setName(name)
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(onClick = {
                            vm.editCategory(goToGategories)
                        }) {
                            Text(text = "Edit")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { backToCategories() }) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    }
}
