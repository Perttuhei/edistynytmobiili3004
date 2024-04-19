package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.AccountDatabase
import com.example.edistynytmobiili3004.DbProvider
import com.example.edistynytmobiili3004.api.authService
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.AddCategoryReq
import com.example.edistynytmobiili3004.model.AddCategoryState
import com.example.edistynytmobiili3004.model.CategoriesResponse
import com.example.edistynytmobiili3004.model.CategoriesState
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.model.CategoryState
import com.example.edistynytmobiili3004.model.DeleteCategoryState
import com.example.edistynytmobiili3004.model.LogoutState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoriesViewModel(private val db: AccountDatabase = DbProvider.db) : ViewModel() {


    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    private val _deleteCategoryState = mutableStateOf(DeleteCategoryState())
    val deleteCategoryState: State<DeleteCategoryState> = _deleteCategoryState

    private val _addCategoryState = mutableStateOf(AddCategoryState())
    val addCategoryState: State<AddCategoryState> = _addCategoryState

    private val _logoutState = mutableStateOf(LogoutState())
    val logoutState: State<LogoutState> = _logoutState

    init {
        getCategories()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                 _logoutState.value = _logoutState.value.copy(loading = true)
                val accessToken = db.accountDao().getToken()
                accessToken?.let {
                    authService.logout("Bearer $it")
                    db.accountDao().removeTokens()
                }
            } catch (e: Exception) {
                _logoutState.value = _logoutState.value.copy(err = e.toString())
            }finally {
                _logoutState.value = _logoutState.value.copy(loading = false)
            }
        }
    }

    fun createCategory() {
        viewModelScope.launch {
            try {
                _addCategoryState.value = _addCategoryState.value.copy(loading = true)
                val res = categoriesService.createCategory(
                    AddCategoryReq(
                        name = _addCategoryState.value.name
                    )
                )
                _categoriesState.value = categoriesState.value.copy(list=_categoriesState.value.list + res)
                toggleAddCategory()
            } catch (e: Exception) {
                _addCategoryState.value = _addCategoryState.value.copy(err = e.toString())
            } finally {
                _addCategoryState.value = _addCategoryState.value.copy(loading = false)
            }
        }
    }

    fun setName(newName: String) {
        _addCategoryState.value = _addCategoryState.value.copy(name = newName)
    }

    fun toggleAddCategory() {
        _categoriesState.value =
            _categoriesState.value.copy(isAddingCategory = !_categoriesState.value.isAddingCategory)
    }

    fun clearErr() {
        _deleteCategoryState.value = _deleteCategoryState.value.copy(err = null)
    }

    fun verifyCategoryRemoval(categoryId: Int) {
        _deleteCategoryState.value = _deleteCategoryState.value.copy(id = categoryId)
    }

    fun deleteCategoryById(categoryId: Int) {

        viewModelScope.launch {
            try {
                categoriesService.removeCategory(categoryId)
                val listOfCategories = _categoriesState.value.list.filter {
                    // jos tämä ehto on totta, menee vuorossa oleva item listaan
                    // jos tämä ehto ei ole totta, jää itemi listasta pois
                    categoryId != it.id
                }


                _categoriesState.value = _categoriesState.value.copy(list = listOfCategories)
                _deleteCategoryState.value = _deleteCategoryState.value.copy(id = 0)
            } catch (e: Exception) {
                _deleteCategoryState.value = deleteCategoryState.value.copy(err = e.toString())
            } finally {
                //
            }
        }

    }

    private suspend fun waitForCategories() {
        delay(2000)
    }

    private fun getCategories() {

        viewModelScope.launch {
            try {
                Log.d("juhani", "in getCategories:: starting to fetch data")
                _categoriesState.value = _categoriesState.value.copy(loading = true)
                val response = categoriesService.getCategories()
                _categoriesState.value = categoriesState.value.copy(

                    list = response.categories

                )

                Log.d("juhani", "in getCategories:: done  fetching data")
            } catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(err = e.message)
            } finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
            }

        }
    }


}