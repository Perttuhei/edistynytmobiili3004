package com.example.edistynytmobiili3004

sealed  class Screen(val route: String) {
    object Register: Screen("RegisterScreen")
    object Login: Screen("loginScreen")
    object Categories: Screen("categoriesScreen")
    object EditCategory: Screen("editCategoryScreen/{categoryId}")
    object RentalItems: Screen("RentalItemsScreen/{categoryId}")
    object EditItems: Screen("editItemScreen/{rental_item_id}")
    object Posts: Screen("postsScreen")
}