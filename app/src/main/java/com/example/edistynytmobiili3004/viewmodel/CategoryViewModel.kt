package com.example.edistynytmobiili3004.viewmodel


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.AccountDatabase
import com.example.edistynytmobiili3004.DbProvider
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.CategoryState
import com.example.edistynytmobiili3004.model.EditCategoryReq
import kotlinx.coroutines.launch

class CategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    private val db: AccountDatabase = DbProvider.db

    private val _categoryState = mutableStateOf(CategoryState())
    val categoryState: State<CategoryState> = _categoryState

    fun setOk(status: Boolean) {
        _categoryState.value = _categoryState.value.copy(ok=status)
    }

    fun editCategory() {
        viewModelScope.launch {
            try {
                val accessToken = db.accountDao().getToken() ?: throw Exception("Kirjaudu sisään muokataksesi kategoriaa")
                accessToken.let {
                    val res = authService.account("Bearer $it")
                    if(res.userId > 0) {
                        _categoryState.value = _categoryState.value.copy(loading = true)
                        categoriesService.editCategory(
                            _categoryId,
                            EditCategoryReq(name = _categoryState.value.item.name), "Bearer $it"
                        )
                        setOk(true)
                    }
                }
            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(err=e.toString())
            } finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }

    private fun getCategory() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)
                val res = categoriesService.getCategory(_categoryId)
                _categoryState.value = _categoryState.value.copy(item = res.category)
            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(err = e.toString())
            } finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }
    init {
        getCategory()
    }
    fun setName(newName: String) {
        val item = _categoryState.value.item.copy(name = newName)
        _categoryState.value = _categoryState.value.copy(item = item)
    }
}