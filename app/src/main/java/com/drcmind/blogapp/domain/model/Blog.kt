package com.drcmind.blogapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Blog(
    val id : String = "",
    val title : String = "",
    val content : String = "",
    val thumbnail : String = "",
    val isFavorite : Boolean = false,
    val user: User? = null,
    @ServerTimestamp val createdDate : Date? = null
)
