package com.drcmind.blogapp.ui

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drcmind.blogapp.data.repository.BlogRepository
import com.drcmind.blogapp.domain.model.SignInResult
import com.drcmind.blogapp.domain.model.User
import com.drcmind.blogapp.util.Result
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: BlogRepository
) : ViewModel(){
    val uiState = mutableStateOf(UiState())

    init {
        getSignedInUser()
        viewModelScope.launch {
            repository.getBlogs().collect{result->
                when(result){
                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            blogs = result.data!!
                        )
                    }
                    else ->{}
                }
            }
        }
    }

    fun onSignInResult(result : SignInResult){
        uiState.value = uiState.value.copy(
            isSignInSuccessfull = result.data != null,
            signinError = result.errorMessage,
            currentUser = result.data
        )
    }

    private fun getSignedInUser(){
        viewModelScope.launch {
            repository.getSignedInUser().collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = result.data
                        )
                    }
                    is Result.Error->{
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = null,
                            signinError = result.e?.message
                        )
                    }
                }
            }
        }
    }

    fun signOut(oneTapClient : SignInClient){
        viewModelScope.launch {
            repository.signOut(oneTapClient).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = null
                        )
                    }
                    is Result.Error->{
                        uiState.value = uiState.value.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun resetSignInState(){
        uiState.value = uiState.value.copy(
            isSignInSuccessfull = false,
            signinError = null
        )
    }

    fun onAddBlog(
        title : String, content : String, thumbnail : Uri, user: User
    ){
        viewModelScope.launch {
            repository.addBlog(title, content, thumbnail, user).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(isLoading = true)
                    }
                    else -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun updateBlog(
        id : String, title: String, content: String, thumbnail: Uri
    ){
        viewModelScope.launch {
            repository.updateBlog(
                id, title, content, thumbnail
            ).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(isLoading = true)
                    }
                    else -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun deleteBlog(id: String){
        viewModelScope.launch {
            repository.deleteBlog(id).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(isLoading = true)
                    }
                    else -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

}