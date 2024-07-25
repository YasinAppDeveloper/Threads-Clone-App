package com.dreamcoder.threadscloneapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dreamcoder.threadscloneapp.mvvm.FetchPostedThreadImages

import com.dreamcoder.threadscloneapp.navigation.Routes
import com.dreamcoder.threadscloneapp.util.SharePerfDataStore

@Composable
fun Profile(
    fetchPostedImages:FetchPostedThreadImages = hiltViewModel(),
    navHostController: NavHostController
) {
    val images by remember { derivedStateOf { fetchPostedImages.images } }
    val loading by fetchPostedImages.loading.collectAsState()

    LaunchedEffect(Unit) {
        fetchPostedImages.fetchUserPostImages()
    }

    val context = LocalContext.current

    LazyColumn {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(15.dp)
            ) {

                val (text, logo, username,
                    bio, followers, following,
                    spacer, button) = createRefs()


                Text(text = SharePerfDataStore.getName(context),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                Image(
                    painter = rememberImagePainter(
                        data =
                        SharePerfDataStore.getUserprofileImage(context)
                    ),
                    contentDescription = "profileImage",
                    modifier = Modifier
                        .size(80.dp)
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .clickable {

                        }
                        .clip(CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop

                )

                Text(text = SharePerfDataStore.getUserName(context),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.constrainAs(username) {
                        start.linkTo(parent.start)
                        top.linkTo(text.bottom)
                    }
                )

                Text(text = SharePerfDataStore.getUserBio(context),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.constrainAs(bio) {
                        start.linkTo(parent.start)
                        top.linkTo(username.bottom)
                    }
                )

                Text(text = "Followers",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 5.dp)
                        .constrainAs(followers) {
                            start.linkTo(parent.start)
                            top.linkTo(bio.bottom)
                        }
                )

                Text(text = "Following",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 5.dp)
                        .constrainAs(following) {
                            start.linkTo(parent.start)
                            top.linkTo(followers.bottom)
                        }
                )

                ElevatedButton(
                    onClick = {
                          fetchPostedImages.signOut()
                          navHostController.navigate(Routes.Register.route){
                              popUpTo(navHostController.graph.id)
                              launchSingleTop = true
                          }
                    },
                    modifier = Modifier.padding(top = 15.dp)
                        .constrainAs(button){
                            top.linkTo(following.bottom)
                            start.linkTo(parent.start)
                        }

                ) {
                    Text(text = "Logout")
                }

            }
        }
        items(images){imagesList->
            LoadImage(imageUrl = imagesList)
        }
    }
}

@Composable
fun LoadImage(imageUrl:String) {
    Image(
        painter = rememberImagePainter(imageUrl),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}