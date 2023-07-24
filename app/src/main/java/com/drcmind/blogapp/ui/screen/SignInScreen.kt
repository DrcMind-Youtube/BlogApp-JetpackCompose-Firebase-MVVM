package com.drcmind.blogapp.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drcmind.blogapp.domain.model.User

@Composable
fun SignInScreen(
    isLoading : Boolean,
    currentUser: User?,
    onSignInClick : ()-> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Star, 
                contentDescription = null,
                modifier = Modifier.size(180.dp),
                tint = Color.LightGray
            )
            if(isLoading || currentUser == null){
                Button(onClick = onSignInClick) {
                    Text(text = "Connexion")
                }
            }
            
        }
    }
}