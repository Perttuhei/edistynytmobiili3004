package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.AccountDatabase
import com.example.edistynytmobiili3004.DbProvider
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.api.itemsService
import com.example.edistynytmobiili3004.model.EditItemReq
import com.example.edistynytmobiili3004.model.ItemState
import kotlinx.coroutines.launch
class RentalItemViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _itemId = savedStateHandle.get<String>("rental_item_id")?.toIntOrNull() ?: 0
    private val db: AccountDatabase = DbProvider.db

    private val _itemState = mutableStateOf(ItemState())
    val itemState: State<ItemState> = _itemState

    fun setOk(status: Boolean) {
        _itemState.value = _itemState.value.copy(ok=status)
    }

    fun editItem() {
        viewModelScope.launch {
            try {
                Log.d("perttu", "editItem1 _itemId = ${_itemId}")
                _itemState.value = _itemState.value.copy(loading = true)
                val accessToken = db.accountDao().getToken() ?: throw Exception("Kirjaudu sisään muokataksesi itemiä")
                accessToken.let {
                    val res = authService.account("Bearer $it")
                    if(res.userId > 0) {
                        itemsService.editItem(
                            _itemId,
                            EditItemReq(name = _itemState.value.item.name),
                            bearerToken = "Bearer $it")
                        setOk(true)
                    }
                }
            } catch (e: Exception) {
                _itemState.value = _itemState.value.copy(err=e.toString())
            } finally {
                _itemState.value = _itemState.value.copy(loading = false)
            }
        }
    }

    private fun getItem() {
        viewModelScope.launch {
            try {
                _itemState.value = _itemState.value.copy(loading = true)
                // jos tietokannasta palautetaan enemmän kuin 0 riviä niin true
                val user: Boolean = db.accountDao().getAccount() > 0
                Log.d("perttu", "getItem user = ${user}")
                if (user) {
                    val res = itemsService.getItem(itemId = _itemId)
                    _itemState.value = _itemState.value.copy(item = res)
                    //Log.d("perttu", "itemstate ${res}")
                }
            } catch (e: Exception) {
                Log.d("perttu", "getItem _itemId = ${_itemId}")
                _itemState.value = _itemState.value.copy(err = e.toString())
            } finally {
                _itemState.value = _itemState.value.copy(loading = false)
            }
        }
    }


    init {
        getItem()
    }

    fun setName(newName: String) {
        val item = _itemState.value.item.copy(name = newName)
        _itemState.value = _itemState.value.copy(item = item)
    }

}
