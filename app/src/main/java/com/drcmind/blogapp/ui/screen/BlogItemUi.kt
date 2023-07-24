package com.drcmind.blogapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.drcmind.blogapp.R
import com.drcmind.blogapp.domain.model.Blog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BlogItemUi(
    blog: Blog,
    onNavigateToBlogDetailsScreen : ()->Unit
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onNavigateToBlogDetailsScreen()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(modifier = Modifier.size(height = 90.dp, width = 120.dp)
        ) {
            AsyncImage(
                model = blog.thumbnail,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = if(blog.createdDate==null) "Synchronisation" else
                    SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(blog.createdDate),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = blog.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = "${(1..999).random()}", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlofItemUiPreview(){
    BlogItemUi(blog = Blog("","Blablablaaaaaaaaaaaaaaaaaaa", createdDate = Calendar.getInstance().time)) {
        
    }
}