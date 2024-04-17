package com.example.edistynytmobiili3004

sealed  class Screen(val route: String) {
    object Login: Screen("loginScreen")
    object Categories: Screen("categoriesScreen")
    // tähän kaikki loputkin screenit ja niiden routet
}