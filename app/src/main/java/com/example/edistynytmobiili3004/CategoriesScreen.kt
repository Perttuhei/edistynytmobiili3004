package com.example.edistynytmobiili3004

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.viewmodel.CategoriesViewModel

@Composable
fun RandomImage() {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://picsum.photos/300")
            .build(),
        contentDescription = "random image"
    )
}

@Composable
fun AddCategoryDialog(
    addCategory: () -> Unit,
    name: String,
    setName: (String) -> Unit,
    closeDialog: () -> Unit
) {
    AlertDialog(

        onDismissRequest = { closeDialog() }, confirmButton = {
            TextButton(onClick = { addCategory() }) {
                Text("Save Category")
            }
        }, title = {
            Text("Add Category")
        },

        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    setName(newName)
                },
                placeholder = { Text("Category Name") })
        })
}

@Composable
fun ConfirmCategoryDelete(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    clearErr: () -> Unit,
    errStr: String?
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = errStr) {

        errStr?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            clearErr()
        }
    }

    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = {
        TextButton(onClick = { onConfirm() }) {
            Text("Delete")
        }


    }, dismissButton = {
        TextButton(onClick = { onCancel() }) {
            Text(text = "Cancel")
        }
    }, title = {
        Text(text = "Are you sure?")
    }, text = {
        Text(text = "Are you sure you want to delete this cateogry?")
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onMenuClick: () -> Unit, navigateToEditCategory: (Int) -> Unit) {
    val categoriesVm: CategoriesViewModel = viewModel()

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = { categoriesVm.toggleAddCategory() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Category")
            }
        },
        topBar = {

            TopAppBar(title = { Text(text = "Categories") }, navigationIcon = {
                IconButton(onClick = { onMenuClick() }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                }
            })
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                categoriesVm.categoriesState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                categoriesVm.categoriesState.value.err != null -> Text(text = "Virhe: ${categoriesVm.categoriesState.value.err}")


                categoriesVm.categoriesState.value.isAddingCategory -> AddCategoryDialog(addCategory = {
                    categoriesVm.createCategory()
                },
                    name = categoriesVm.addCategoryState.value.name, setName = { newName ->
                        categoriesVm.setName(newName)
                    }, closeDialog = {
                        categoriesVm.toggleAddCategory()
                    })

                categoriesVm.deleteCategoryState.value.id > 0 -> ConfirmCategoryDelete(onConfirm = {
                    categoriesVm.deleteCategoryById(categoriesVm.deleteCategoryState.value.id)

                }, onCancel = {
                    categoriesVm.verifyCategoryRemoval(0)
                }, clearErr = {
                    categoriesVm.clearErr()
                },
                    categoriesVm.deleteCategoryState.value.err
                )

                else -> LazyColumn {
                    items(categoriesVm.categoriesState.value.list) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { navigateToEditCategory(it.id) },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                RandomImage()
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    IconButton(onClick = { categoriesVm.verifyCategoryRemoval(it.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                    IconButton(onClick = { navigateToEditCategory(it.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}