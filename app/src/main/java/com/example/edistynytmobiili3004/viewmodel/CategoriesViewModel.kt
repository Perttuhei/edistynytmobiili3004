package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.AddCategoryReq
import com.example.edistynytmobiili3004.model.CategoriesResponse
import com.example.edistynytmobiili3004.model.CategoriesState
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.model.CategoryState
import com.example.edistynytmobiili3004.model.DeleteCategoryState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoriesViewModel: ViewModel() {

    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    private val _deleteCategoryState = mutableStateOf(DeleteCategoryState())
    val deleteCategoryState: State<DeleteCategoryState> = _deleteCategoryState

    private val _categoryState = mutableStateOf(CategoryState())
    val categoryState: State<CategoryState> = _categoryState

    init {
        getCategories()
    }

    fun AddCategory(newCategory: AddCategoryReq, goToCategories: () -> Unit) {
        viewModelScope.launch {
            try {
                _categoriesState.value = _categoriesState.value.copy(loading = true)

                // Tehdään POST-pyyntö palvelimelle
                val response = categoriesService.addCategory(newCategory)

                // Lisätään vastauksen kategoria categoriesState-listaan
                val updatedList = _categoriesState.value.list.toMutableList()
                updatedList.add(response.category)
                _categoriesState.value = _categoriesState.value.copy(list = updatedList)


                goToCategories()

            } catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(err = e.toString())
            } finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
            }
        }
    }

    fun verifyCategoryRemoval(categoryId: Int) {
        _deleteCategoryState.value = _deleteCategoryState.value.copy(id=categoryId)
    }

    fun deleteCategoryById(categoryId: Int) {

        viewModelScope.launch {
            try {
                categoriesService.removeCategory(categoryId)
                val listOfCategories = _categoriesState.value.list.filter {
                    categoryId != it.id
                }
                _categoriesState.value = _categoriesState.value.copy(list=listOfCategories)
            } catch (e: Exception) {
                Log.d("virhe", e.toString())
            } finally {

            }
        }

        //val listOfCategories = _categoriesState.value.list.filter {
        //    categoryId != it.id
        //}
        //_categoriesState.value = _categoriesState.value.copy(list=listOfCategories)
    }

    private suspend fun waitForCategories() {
        delay(2000)
    }

    private fun getCategories() {
        try {
            viewModelScope.launch {
                _categoriesState.value = _categoriesState.value.copy(loading = true)
                val response = categoriesService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(
                    list = response.categories
                )
            }
        } catch (e: Exception) {
            _categoriesState.value = _categoriesState.value.copy(err = e.message)
        } finally {
            _categoriesState.value = _categoriesState.value.copy(loading = false)
        }
    }


}