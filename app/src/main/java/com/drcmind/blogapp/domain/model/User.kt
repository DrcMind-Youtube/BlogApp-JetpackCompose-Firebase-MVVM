package com.drcmind.blogapp.domain.model

data class User(
    val userId : String = "",
    val username : String? = null,
    val profilePictureUrl : String? = null
)
