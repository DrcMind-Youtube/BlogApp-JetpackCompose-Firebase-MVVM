package com.drcmind.blogapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.drcmind.blogapp.R
import com.drcmind.blogapp.domain.model.Blog
import com.drcmind.blogapp.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: User?,
    blogs : List<Blog>,
    isLoading : Boolean,
    onSignOut : ()->Unit,
    onNavigateToBlogDetailsScreen: (Blog)->Unit,
    onNavigateToUpdateBlogScreen : ()->Unit,
    onNavigateToSigninScreen : ()->Unit
){

    var isDropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var query by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = currentUser){
        if(currentUser==null){
            onNavigateToSigninScreen()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "BlogApp", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                },
                actions = {
                    AsyncImage(
                        model = currentUser?.profilePictureUrl,
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.account),
                        error = painterResource(id = R.drawable.account),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                isDropDownMenuExpanded = !isDropDownMenuExpanded
                            }
                    )
                    DropdownMenu(
                        expanded = isDropDownMenuExpanded,
                        onDismissRequest = { isDropDownMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "${currentUser?.username}")
                                Text(text = "Déconnexion")
                            } },
                            onClick = {
                                onSignOut()
                                isDropDownMenuExpanded = false
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToUpdateBlogScreen) {
                Icon(imageVector = Icons.Filled.Create, contentDescription = null)
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if(isLoading){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            OutlinedTextField(
                value = query ,
                onValueChange = { text->
                    query = text
                },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Rechercher")},
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null)
                    }
                }
            )

            if(blogs.isEmpty()){
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 202.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = Color.Red
                        )
                        Text(
                            text = "Aucun blog disponnible",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ){
                    items(blogs){blog->
                        BlogItemUi(
                            blog = blog,
                            onNavigateToBlogDetailsScreen = {
                                onNavigateToBlogDetailsScreen(blog)
                            }
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen(currentUser = null, blogs = emptyList(),isLoading = false, {},{},{}) {

    }
}