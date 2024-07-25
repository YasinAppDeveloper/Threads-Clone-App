package com.dreamcoder.threadscloneapp.screen

import UserModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.dreamcoder.threads.viewmodel.RegisterViewModel

@Composable
@Preview
fun Search(
    userViewModel: RegisterViewModel = hiltViewModel()
) {
    val users by remember { derivedStateOf { userViewModel.getFilteredUsers() } }
    val loading by userViewModel.loading.collectAsState()
    val searchQuery by userViewModel.searchQuery.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        Column {
            SearchBar(
                query = searchQuery,
                onQueryChange = userViewModel::updateSearchQuery
            )

            if (loading) {
                // Show loading indicator
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    items(users){item->
                        UserItem(user = item)
                    }
                }
            }
        }
    }

}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text(text = "Search users") },
        singleLine = true
    )
}

@Composable
fun UserItem(user: UserModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Image(
            painter = rememberImagePainter(data =user.imageProfile),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)

        )

        Column(
            modifier = Modifier.padding(start = 15.dp, bottom = 5.dp)
        ) {
            Text(text = user.name, fontWeight = FontWeight.Bold)
            Text(text = user.userName)
        }
    }
}