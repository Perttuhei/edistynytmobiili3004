package com.example.edistynytmobiili3004

import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.viewmodel.RentalItemsViewModel

@Composable
fun AddItemDialog(
    addItem: () -> Unit,
    name: String,
    setName: (String) -> Unit,
    closeDialog: () -> Unit,
    clearErr: () -> Unit,
    errStr: String?
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = errStr) {

        errStr?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Log.d("perttu", "virhe ${it}")
            clearErr()
        }
    }
    AlertDialog(

        onDismissRequest = { closeDialog() }, confirmButton = {
            TextButton(onClick = { addItem() }) {
                Text(stringResource(id = R.string.save_item))
            }
        }, title = {
            Text(stringResource(id = R.string.add_item))
        },

        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    setName(newName)
                },
                placeholder = { Text(stringResource(id = R.string.item_name)) })
        })
}

@Composable
fun ConfirmItemDelete(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    clearErr: () -> Unit,
    errStr: String?
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = errStr) {

        errStr?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Log.d("perttu", "virhe ${it}")
            clearErr()
        }
    }

    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = {
        TextButton(onClick = { onConfirm() }) {
            Text(stringResource(id = R.string.delete))
        }


    }, dismissButton = {
        TextButton(onClick = { onCancel() }) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }, title = {
        Text(text = stringResource(id = R.string.are_you_sure))
    }, text = {
        Text(text = stringResource(id = R.string.Are_you_sure_you_want_to_delete_this_item))
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalItemsScreen(onMenuClick: () -> Unit, navigateToEditItem: (Int) -> Unit) {
    val vm: RentalItemsViewModel = viewModel()

    val context = LocalContext.current

    LaunchedEffect(key1 = vm.rentItemState.value.ok) {
        val text = R.string.item_rent_ok
        if(vm.rentItemState.value.ok) {
            vm.setrentOk(false)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.toggleAddItem() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Item")
            }
        },
        topBar = {

            TopAppBar(title = { Text(text = stringResource(id = R.string.items)) }, navigationIcon = {
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
                vm.itemsState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                vm.itemsState.value.err != null -> Text(text = "Virhe: ${vm.itemsState.value.err}")


                vm.itemsState.value.isAddingItem -> AddItemDialog(addItem = {
                    vm.createItem()
                },
                    name = vm.addItemState.value.name, setName = { newName ->
                        vm.setName(newName)
                    }, closeDialog = {
                        vm.toggleAddItem()
                    }, clearErr = {
                        vm.clearErr()
                    }, vm.addItemState.value.err)

                vm.deleteItemState.value.id > 0 -> ConfirmItemDelete(onConfirm = {
                    vm.deleteItemById(vm.deleteItemState.value.id)

                }, onCancel = {
                    vm.verifyItemRemoval(0)
                }, clearErr = {
                    vm.clearErr()
                },
                    vm.deleteItemState.value.err
                )

                else -> LazyColumn {
                    items(vm.itemsState.value.list) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        navigateToEditItem(it.id)
                                        Log.d("perttu", "navigateToEditItem ID ${it.id}")
                                    },
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
                                    IconButton(onClick = { vm.verifyItemRemoval(it.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                    IconButton(onClick = { navigateToEditItem(it.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit"
                                        )
                                    }
                                    TextButton(onClick = { vm.rentItem(it.id) }) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Rent",
                                                modifier = Modifier.padding(end = 4.dp)
                                            )
                                            Text(text = "Rent ${it.name}")
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
}