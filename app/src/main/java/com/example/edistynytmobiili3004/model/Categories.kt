package com.example.edistynytmobiili3004.model

data class CategoriesState(val List: List<CategoryItem> = emptyList(), val loading: Boolean = false)

data class CategoryItem(val id: Int = 0, val name: String = "")

