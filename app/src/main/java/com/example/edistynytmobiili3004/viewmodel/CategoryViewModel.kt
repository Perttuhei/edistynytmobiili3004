package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.edistynytmobiili3004.model.CategoryState

class CategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    private val _categoryState = mutableListOf(CategoryState())
    val categoryState: MutableList<CategoryState> = _categoryState

    init {
        Log.d("Perttu", "$categoryId")
    }
}