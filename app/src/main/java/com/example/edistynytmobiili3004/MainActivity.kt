package com.example.edistynytmobiili3004

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.ui.theme.EdistynytMobiili3004Theme
import com.example.edistynytmobiili3004.viewmodel.CategoriesViewModel
import com.example.edistynytmobiili3004.viewmodel.CategoryViewModel
import com.example.edistynytmobiili3004.viewmodel.DrawerViewModel
import com.example.edistynytmobiili3004.viewmodel.LoginViewModel
import com.example.edistynytmobiili3004.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EdistynytMobiili3004Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),

                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val drawerVm: DrawerViewModel = viewModel()
                    val registerVm: RegisterViewModel = viewModel()
                    val context = LocalContext.current
                    var categoryId: Int = 0

                    LaunchedEffect(key1 = drawerVm.logoutState.value.err) {
                        drawerVm.logoutState.value.err?.let {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    }

                    LaunchedEffect(key1 = drawerVm.logoutState.value.logoutOk) {
                        if (drawerVm.logoutState.value.logoutOk) {
                            drawerVm.setLogout(false)
                            navController.navigate("loginScreen") {
                                popUpTo("loginScreen") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    ModalNavigationDrawer(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                NavigationDrawerItem(
                                    label = { Text(text = "Categories") },
                                    selected = true,
                                    onClick = { scope.launch {
                                        navController.navigate("categoriesScreen")
                                        drawerState.close()
                                    } },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = "Home"
                                        )
                                    })
                                NavigationDrawerItem(
                                    label = { Text(text = "Login") },
                                    selected = false,
                                    onClick = {drawerVm.logout()
                                        scope.launch {
                                            navController.navigate("loginScreen")
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = "Login"
                                        )
                                    })
                                NavigationDrawerItem(
                                    label = { Text(text = "Logout") },
                                    selected = false,
                                    onClick = { drawerVm.logout()
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowLeft,
                                            contentDescription = "Logout"
                                        )
                                    })
                                NavigationDrawerItem(
                                    label = { Text(text = "Register") },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            navController.navigate("RegisterScreen")
                                            drawerState.close()

                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Register"
                                        )
                                    })
                            }
                        }, drawerState = drawerState
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "loginScreen"
                        ) {

                            composable(route = "postsScreen") {
                                PostsScreen()
                            }
                            composable("loginScreen") {
                                LoginScreen(goToCategories = {
                                    navController.navigate("categoriesScreen")
                                }, onMenuClick = {scope.launch {
                                    drawerState.open()
                                }})
                            }
                            composable(route = "categoriesScreen") {
                                CategoriesScreen(onMenuClick = {scope.launch {
                                        drawerState.open()
                                    }
                                }, navigateToEditCategory = {
                                    navController.navigate("editCategoryScreen/${it}")
                                }, goToItems = {
                                    // tallennetaan kategorian id muuttujaan myöhempää käyttöä varten
                                    // tarvitaan editItemScreeniltä siirryttäessä takaisin kategoriaan
                                    categoryId = it
                                    navController.navigate("RentalItemsScreen/${it}")})
                            }
                            composable("editCategoryScreen/{categoryId}") {
                                EditCategoryScreen(backToCategories = {
                                    navController.navigateUp()
                                }, goToCategories = {
                                    navController.navigate("categoriesScreen")
                                })
                            }
                            composable(route = "RentalItemsScreen/{categoryId}") {
                                RentalItemsScreen(onMenuClick = {scope.launch {
                                        drawerState.open()
                                    }},
                                    navigateToEditItem = {
                                        navController.navigate("editItemScreen/${it}")}
                                )
                            }
                            composable("editItemScreen/{rental_item_id}") {
                                EditItemScreen(backToItems = {
                                    navController.navigateUp()
                                }, goToItems = {
                                    navController.navigate("RentalItemsScreen/${it}")
                                })
                            }
                            composable("RegisterScreen") {
                                RegisterScreen(goToLogin = {navController.navigate("loginScreen")})
                            }

                        }
                    }
                }
            }
        }
    }
}

