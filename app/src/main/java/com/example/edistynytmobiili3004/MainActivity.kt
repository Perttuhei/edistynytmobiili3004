package com.example.edistynytmobiili3004

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiili3004.ui.theme.EdistynytMobiili3004Theme
import com.example.edistynytmobiili3004.viewmodel.DrawerViewModel
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
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    ModalNavigationDrawer(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        drawerContent = {
                            ModalDrawerSheet {
                                Text(text = stringResource(id = R.string.menu), modifier = Modifier.padding(16.dp))
                                Spacer(modifier = Modifier.height(16.dp))
                                val isSelected = remember { mutableStateOf("") }
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(id = R.string.categories)) },
                                    selected = isSelected.value == "Categories",
                                    onClick = { scope.launch {
                                        navController.navigate(Screen.Categories.route)
                                        drawerState.close()
                                        isSelected.value = "Categories"
                                    } },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = "Home"
                                        )
                                    })
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(id = R.string.login)) },
                                    selected = isSelected.value == "Login",
                                    onClick = {
                                        scope.launch {
                                            navController.navigate(Screen.Login.route)
                                            drawerState.close()
                                            isSelected.value = "Login"
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = "Login"
                                        )
                                    })
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(id = R.string.logout)) },
                                    selected = isSelected.value == "Logout",
                                    onClick = { drawerVm.logout()
                                        scope.launch {
                                            drawerState.close()
                                            isSelected.value = "Logout"
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowLeft,
                                            contentDescription = "Logout"
                                        )
                                    })
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(id = R.string.register)) },
                                    selected = isSelected.value == "Register",
                                    onClick = {
                                        scope.launch {
                                            navController.navigate(Screen.Register.route)
                                            drawerState.close()
                                            isSelected.value = "Register"
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
                            startDestination = Screen.Login.route
                        ) {

                            composable(route = Screen.Posts.route) {
                                PostsScreen()
                            }
                            composable(Screen.Login.route) {
                                LoginScreen(goToCategories = {
                                    navController.navigate(Screen.Categories.route)
                                }, onMenuClick = {scope.launch {
                                    drawerState.open()
                                }})
                            }
                            composable(route = Screen.Categories.route) {
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
                            composable(Screen.EditCategory.route) {
                                EditCategoryScreen(backToCategories = {
                                    navController.navigateUp()
                                }, goToCategories = {
                                    navController.navigate(Screen.Categories.route)
                                })
                            }
                            composable(route = Screen.RentalItems.route) {
                                RentalItemsScreen(onMenuClick = {scope.launch {
                                        drawerState.open()
                                    }},
                                    navigateToEditItem = {
                                        navController.navigate("editItemScreen/${it}")}
                                )
                            }
                            composable(Screen.EditItems.route) {
                                EditItemScreen(backToItems = {
                                    navController.navigateUp()
                                }, goToItems = {
                                    navController.navigate("RentalItemsScreen/${it}")
                                })
                            }
                            composable(Screen.Register.route) {
                                RegisterScreen(goToLogin = {navController.navigate(Screen.Login.route)},
                                    onMenuClick = {scope.launch {
                                        drawerState.open()
                                    }})
                            }
                        }
                    }
                }
            }
        }
    }
}

