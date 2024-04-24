package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.AccountDatabase
import com.example.edistynytmobiili3004.DbProvider
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.api.itemsService
import com.example.edistynytmobiili3004.model.AccountRes
import com.example.edistynytmobiili3004.model.AccountState
import com.example.edistynytmobiili3004.model.AddItemReq
import com.example.edistynytmobiili3004.model.AddItemState
import com.example.edistynytmobiili3004.model.CategoriesState
import com.example.edistynytmobiili3004.model.CategoryState
import com.example.edistynytmobiili3004.model.DeleteItemState
import com.example.edistynytmobiili3004.model.ItemsState
import com.example.edistynytmobiili3004.model.RentItemReq
import com.example.edistynytmobiili3004.model.RentItemState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RentalItemsViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

    private val _categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    private val db: AccountDatabase = DbProvider.db

    private val _itemsState = mutableStateOf(ItemsState())
    val itemsState: State<ItemsState> = _itemsState

    private val _deleteItemState = mutableStateOf(DeleteItemState())
    val deleteItemState: State<DeleteItemState> = _deleteItemState

    private val _addItemState = mutableStateOf(AddItemState())
    val addItemState: State<AddItemState> = _addItemState

    private val _rentItemState = mutableStateOf(RentItemState())
    val rentItemState: State<RentItemState> = _rentItemState

    init {
        Log.d("perttu", "itemsviewmodel categoryid ${_categoryId}")
        getItems()
    }

    fun createItem() {
        viewModelScope.launch {
            try {
                val accessToken = db.accountDao().getToken() ?: throw Exception("Vain kirjautuneet käyttäjät voivat lisätä tavaroita")
                accessToken.let {
                    val res = authService.account("Bearer $it")
                    if(res.userId > 0) {
                        _addItemState.value = _addItemState.value.copy(loading = true)
                        val userId = db.accountDao().getUserId()
                        Log.d("perttu", "createItem userId ${userId}")
                        val res = itemsService.createItem(categoryId = _categoryId,
                            AddItemReq(
                                _addItemState.value.name, userId), "Bearer $it"
                        )
                        _itemsState.value = itemsState.value.copy(list=_itemsState.value.list + res)
                        toggleAddItem()
                    }
                }
            } catch (e: Exception) {
                _addItemState.value = _addItemState.value.copy(err = e.toString())
            } finally {
                _addItemState.value = _addItemState.value.copy(loading = false)
            }
        }
    }


    fun setName(newName: String) {
        _addItemState.value = _addItemState.value.copy(name = newName)
    }

    fun toggleAddItem() {
        _itemsState.value =
            _itemsState.value.copy(isAddingItem = !_itemsState.value.isAddingItem)
    }

    fun clearErr() {
        _deleteItemState.value = _deleteItemState.value.copy(err = null)
        _addItemState.value = _addItemState.value.copy(err = null)
    }

    fun verifyItemRemoval(itemId: Int) {
        _deleteItemState.value = _deleteItemState.value.copy(id = itemId)
    }

    fun deleteItemById(itemId: Int) {

        viewModelScope.launch {
            try {
                val accessToken = db.accountDao().getToken() ?: throw Exception("Vain kirjautunut käyttäjä voi poistaa tavaran")
                accessToken.let {
                    val res = authService.account("Bearer $it")
                    if(res.userId > 0) {
                        itemsService.removeItem(itemId, "Bearer $it")
                        val listOfItems = _itemsState.value.list.filter {
                            // jos tämä ehto on totta, menee vuorossa oleva item listaan
                            // jos tämä ehto ei ole totta, jää itemi listasta pois
                            itemId != it.id
                        }
                        _itemsState.value = _itemsState.value.copy(list = listOfItems)
                        _deleteItemState.value = _deleteItemState.value.copy(id = 0)
                    }
                }

            } catch (e: Exception) {
                _deleteItemState.value = deleteItemState.value.copy(err = e.toString())
            } finally {
                //
            }
        }
    }
    fun rentItem(itemId: Int) {
        viewModelScope.launch {
            try {
                _rentItemState.value = _rentItemState.value.copy(loading = true)
                val accessToken = db.accountDao().getToken() ?: throw Exception("Vain sisäänkirjautuneet käyttäjät voivat vuokrata tavaran")
                accessToken.let {
                    val res = authService.account("Bearer $it")
                    val userId = db.accountDao().getUserId()
                    Log.d("perttu", "rentitem userid = ${res.userId}, item id = ${itemId}")
                    if(res.userId > 0) {
                        itemsService.rentItem(itemId = itemId, req = RentItemReq(userId = userId), bearerToken = "Bearer $it")
                    }
                }
            } catch (e: Exception) {
                _rentItemState.value = _rentItemState.value.copy(err=e.toString())
            } finally {
                _rentItemState.value = _rentItemState.value.copy(loading = false)
            }
        }
    }

    private suspend fun waitForitems() {
        delay(2000)
    }

    private fun getItems() {
        viewModelScope.launch {
            try {
                Log.d("perttu", "in getitems:: starting to fetch data")
                _itemsState.value = _itemsState.value.copy(loading = true)
                val response = itemsService.getItems(categoryId = _categoryId)
                response.items.forEach { item ->
                    Log.d(
                        "perttu",
                        "Item id: ${item.id}, Name: ${item.name}, Category ID: ${_categoryId}"
                    )}
                _itemsState.value = itemsState.value.copy(

                    list = response.items

                )

                Log.d("perttu", "in getitems:: done  fetching data")
            } catch (e: Exception) {
                _itemsState.value = _itemsState.value.copy(err = e.message)
            } finally {
                _itemsState.value = _itemsState.value.copy(loading = false)
            }
        }
    }
}
