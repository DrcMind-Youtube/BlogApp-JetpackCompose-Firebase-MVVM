package com.drcmind.blogapp.ui

import com.drcmind.blogapp.domain.model.Blog
import com.drcmind.blogapp.domain.model.User

data class UiState(
    val isSignInSuccessfull : Boolean = false,
    val signinError : String? = null,
    val currentUser: User? = null,
    val isLoading : Boolean = false,
    val blogs : List<Blog> = emptyList()
)
