package com.example.edistynytmobiili3004.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.api.postsService
import com.example.edistynytmobiili3004.model.PostsState
import kotlinx.coroutines.launch

class PostsViewModel: ViewModel() {
    private val _postsState = mutableStateOf(PostsState())
    val postState: State<PostsState> = _postsState

    init{
        getPosts()
    }

    private fun getPosts() {
        viewModelScope.launch {
            try {
                _postsState.value = _postsState.value.copy(loading = true)
                val response = postsService.getPosts()
                _postsState.value = _postsState.value.copy(List=response)
            } catch (e: Exception) {
                _postsState.value = _postsState.value.copy(error = e.toString())
            } finally {
                // finally ajetaan aina, onnistuttiin tai epäonnistuttiin
                _postsState.value = _postsState.value.copy(loading = false)
            }
        }
    }

}