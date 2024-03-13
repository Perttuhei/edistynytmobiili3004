package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat.getCategory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.CategoryState
import com.example.edistynytmobiili3004.model.EditCategoryReq
import kotlinx.coroutines.launch

class CategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    private val _categoryState = mutableStateOf(CategoryState())

    val categoryState: State<CategoryState> = _categoryState

    fun setDone(newValue: Boolean) {
        _categoryState.value = _categoryState.value.copy(ok=newValue)
    }

    fun setName(newName: String) {
        _categoryState.value = _categoryState.value.copy(categoryName = newName)
    }


    fun editCategory(goToCategories: () -> Unit) {
        viewModelScope.launch {
            try {

                _categoryState.value = _categoryState.value.copy(loading = true)
                categoriesService.editCategory(
                    categoryId, EditCategoryReq(
                        categoryName = _categoryState.value.categoryName
                    )
                )

                setDone(true)


            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(err = e.toString())
            } finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }

    private fun getCategoryById() {

        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)
                val response = categoriesService.getCategory(categoryId)
                _categoryState.value =
                    _categoryState.value.copy(categoryName = response.category.name)


            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(err = e.toString())
            } finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }


    init {
        getCategoryById()
    }


}