package com.dreamcoder.threadscloneapp.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.dreamcoder.threads.model.ThreadWithUser
import com.dreamcoder.threads.viewmodel.UploadThreadViewModel
import com.dreamcoder.threadscloneapp.R

@Composable
@Preview
fun Home(
    threadViewModel: UploadThreadViewModel = hiltViewModel()
) {

    val fetchThreadState by threadViewModel.listOfThreads.collectAsState()
    val loadingThread by threadViewModel.loading.collectAsState()
    val content = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        threadViewModel.fetchThreads()
    }


    Surface(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        Column(
        ) {

            LazyColumn(
                modifier = Modifier.padding(top = 10.dp)
            ) {

                item {
                    TopLogoAndLoader(loadingThread)
                }

                items(fetchThreadState) { listOfThread ->

                    DisplayPost(listOfThread, content)
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { threadViewModel.listOfThreads.value.size }
            .collect {
                threadViewModel.fetchThreads()
            }
    }
}

@Composable
fun DisplayPost(threadWithUser: ThreadWithUser, context: Context) {


    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(390.dp)
            .padding(top = 10.dp, bottom = 15.dp)

    ) {

        val (
            profileImage,
            time,
            caption,
            username,
            moreIcon,
            horizontalRow,
            like,
            likeCount,
            comment,
            commentCount,
            shareIcon,
            spacer
        ) = createRefs()

        Image(
            painter = rememberImagePainter(data = threadWithUser.user.imageProfile),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(40.dp)
                .padding(start = 10.dp, bottom = 4.dp)
                .clip(CircleShape)
                .constrainAs(profileImage) {
                    top.linkTo(username.top)
                    bottom.linkTo(caption.bottom)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop
        )

        Text(text = threadWithUser.user.userName,
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 15.dp)
                .constrainAs(username) {
                    top.linkTo(parent.top)
                    start.linkTo(profileImage.end, margin = 15.dp)

                }
        )

        Text(text = formatTimestamp(threadWithUser.thread.timeStamp),
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray,
            modifier = Modifier
                .padding(top = 15.dp)
                .constrainAs(time) {
                    top.linkTo(parent.top)
                    bottom.linkTo(username.bottom)
                    start.linkTo(username.end, margin = 5.dp)
                }
        )

        Text(
            text = if (threadWithUser.thread.caption.length > 20) {
                "${threadWithUser.thread.caption.take(20)}..."
            } else {
                threadWithUser.thread.caption
            },
            fontSize = 11.sp,
            maxLines = 1,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 14.dp)
                .constrainAs(caption) {
                    top.linkTo(username.bottom)
                    start.linkTo(profileImage.end, margin = 15.dp)
                }
        )
        Image(
            painter = painterResource(id = R.drawable.baseline_more_horiz_24),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp)
                .clip(CircleShape)
                .constrainAs(moreIcon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, margin = 10.dp)
                }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 45.dp, end = 10.dp)
                .constrainAs(horizontalRow) {
                    top.linkTo(profileImage.bottom)
                }
        ) {
            if (threadWithUser.thread.listOfPost.isEmpty()) {
                Text(
                    text = threadWithUser.thread.caption,
                    modifier = Modifier
                        .padding(
                            top = 15.dp,
                            bottom = 15.dp,
                            start = 15.dp,
                            end = 15.dp
                        )
                        .background(colorResource(id = R.color.back_color))
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(threadWithUser.thread.listOfPost) { imageUrl ->
                        Image(
                            painter = rememberImagePainter(data = imageUrl,
                                builder = {
                                    placeholder(R.drawable.placeholder)
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .width(290.dp)
                                .height(280.dp)
                                .padding(start = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { /* Add your onClick action here */ },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }


        Image(
            painter = painterResource(id = R.drawable.like_icon),
            contentDescription = "profileImage",
            modifier = Modifier
                .padding(start = 45.dp, top = 10.dp)
                .clip(CircleShape)
                .size(22.dp)
                .constrainAs(like) {
                    top.linkTo(horizontalRow.bottom)
                    start.linkTo(parent.start, margin = 10.dp)
                }
                .clickable {
                    Toast
                        .makeText(context, "", Toast.LENGTH_SHORT)
                        .show()
                }

        )

        Text(text = "102",
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 10.dp)
                .constrainAs(likeCount) {
                    top.linkTo(horizontalRow.bottom)
                    bottom.linkTo(like.bottom)
                    start.linkTo(like.end, margin = 7.dp)

                }
        )

        Image(
            painter = painterResource(id = R.drawable.comment_icon),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(35.dp)
                .padding(start = 10.dp, top = 10.dp)
                .clip(CircleShape)
                .constrainAs(comment) {
                    top.linkTo(horizontalRow.bottom)
                    start.linkTo(likeCount.end, margin = 10.dp)
                    bottom.linkTo(likeCount.bottom)
                }
        )

        Text(text = "223",
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 10.dp)
                .constrainAs(commentCount) {
                    top.linkTo(horizontalRow.bottom)
                    bottom.linkTo(comment.bottom)
                    start.linkTo(comment.end, margin = 7.dp)

                }
        )

        Image(
            painter = painterResource(id = R.drawable.share_icon_thread),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(32.dp)
                .padding(start = 10.dp, top = 13.dp)
                .clip(CircleShape)
                .constrainAs(shareIcon) {
                    top.linkTo(horizontalRow.bottom)
                    bottom.linkTo(commentCount.bottom)
                    start.linkTo(commentCount.end, margin = 10.dp)
                }
        )

        Spacer(modifier = Modifier
            .padding(top = 15.dp, bottom = 15.dp)
            .fillMaxWidth()
            .height(0.5.dp)
            .background(color = Color.Gray)
            .constrainAs(spacer) {
                top.linkTo(like.bottom)

            }
        )

    }
}

fun formatTimestamp(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    val years = days / 365

    return when {
        years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
        months > 0 -> "$months month${if (months > 1) "s" else ""} ago"
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
        else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
    }
}

@Composable
fun ImageItem(image: String) {
    Image(
        painter = rememberImagePainter(data = image),
        contentDescription = null,
        modifier = Modifier
            .width(220.dp)
            .height(280.dp)
            .padding(
                5.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable { /* Handle click if needed */ },
        contentScale = ContentScale.Crop
    )
}

@Composable
@Preview
fun HomePreview() {
    Home()
}

@Composable
fun TopLogoAndLoader(loadingThread: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.threads_main_icon),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            if (loadingThread) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(5.dp)
                )
            }
        }


    }
}


