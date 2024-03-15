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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.model.AddCategoryReq
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.viewmodel.CategoriesViewModel
import com.example.edistynytmobiili3004.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(goToCategories: () -> Unit, backToCategories: () -> Unit) {

    val vm: CategoriesViewModel = viewModel()
    val categoryVm: CategoryViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Add new category")
            }, navigationIcon = { IconButton(onClick = { backToCategories() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back to categories")
            }
            })
        }
    ) {

        LaunchedEffect(key1 = categoryVm.categoryState.value.ok) {
            if(categoryVm.categoryState.value.ok) {
                goToCategories()
                categoryVm.setDone(false)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                vm.categoriesState.value.loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center)
                )

                vm.categoriesState.value.err != null -> Text(text = "Virhe: ${vm.categoriesState.value.err}")
                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var categoryName by remember {mutableStateOf("")}
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it
                        },
                        label = { Text(text = "Category Name")})
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(onClick = {
                            vm.AddCategory(newCategory = AddCategoryReq(categoryName = categoryName)) {goToCategories()}
                        }) {
                            Text(text = "Add")
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