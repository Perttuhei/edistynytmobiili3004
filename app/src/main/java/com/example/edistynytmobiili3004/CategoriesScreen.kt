package com.example.edistynytmobiili3004

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.viewmodel.CategoriesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CategoriesScreenPrewiev() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "categories") }, navigationIcon = {
            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        })
    }) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn() {
                items(listOf(CategoryItem(id=1, name="kategoria1"))) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("kuva tähän myöhemmin")
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = it.name, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onMenuClick: () -> Unit) {
    val categoriesVm: CategoriesViewModel = viewModel()

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Categories") }, navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        })
    }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            when {
                categoriesVm.categoriesState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn() {
                    items(categoriesVm.categoriesState.value.list) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("kuva tähän myöhemmin")
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = it.name, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }
            
        }
    }
}