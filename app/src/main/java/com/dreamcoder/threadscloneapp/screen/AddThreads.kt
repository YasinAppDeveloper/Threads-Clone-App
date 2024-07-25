package com.dreamcoder.threadscloneapp.screen

import UiState
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dreamcoder.threads.viewmodel.UploadThreadViewModel
import com.dreamcoder.threadscloneapp.R
import com.dreamcoder.threadscloneapp.navigation.Routes
import com.dreamcoder.threadscloneapp.util.ProgressDialog
import com.dreamcoder.threadscloneapp.util.SharePerfDataStore


@Composable
fun AddThreads(
    threadViewModel: UploadThreadViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val postUiState by threadViewModel.postUiState.collectAsState()
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    var thread by remember { mutableStateOf("")}
    var isDialog by remember {mutableStateOf(false)}
    val context = LocalContext.current


    ConstraintLayout(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(15.dp)
    ) {

        val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else Manifest.permission.READ_EXTERNAL_STORAGE

        val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { uris ->
            selectedImages = uris
        }

        val content = LocalContext.current
        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(content, "Permission is granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(content, "Permission is dined", Toast.LENGTH_SHORT).show()
                }
            }

        val (crossIcon, text, logo, username, editText, attachMedia,
            replyText, button, imageBox, spacer, horizontalRow) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .size(25.dp)
                .constrainAs(crossIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {

                }
        )

        Text(text = "Add Threads",
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(text) {
                top.linkTo(crossIcon.top)
                start.linkTo(crossIcon.end, margin = 15.dp)
                bottom.linkTo(crossIcon.bottom)
            }
        )

        Image(
            painter = rememberImagePainter(
                data =
                SharePerfDataStore.getUserprofileImage(content)
            ),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(35.dp)
                .constrainAs(logo) {
                    top.linkTo(text.bottom, margin = 15.dp)
                    start.linkTo(parent.start)
                }
                .clickable {

                }
                .clip(CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop

        )

        Text(text = SharePerfDataStore.getUserName(content),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.constrainAs(username) {
                start.linkTo(logo.end, margin = 15.dp)
                top.linkTo(logo.top)
                bottom.linkTo(logo.bottom)

            }
        )

        BasicTextFailedMe(hint = "start a thread", value = thread, onValueChange = {
            thread = it
        }, modifier = Modifier
            .constrainAs(editText) {
                top.linkTo(logo.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(top = 10.dp)
            .fillMaxWidth())

        Image(
            painter = painterResource(id = R.drawable.baseline_attachment_24),
            contentDescription = "close",
            modifier = Modifier
                .size(35.dp)
                .padding(top = 10.dp)
                .constrainAs(attachMedia) {
                    top.linkTo(editText.bottom)
                    start.linkTo(parent.start)
                }
                .clickable {
                    val isGranted = ContextCompat.checkSelfPermission(
                        content,
                        permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        multiplePhotoPickerLauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                },

            )

        LazyRow(
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
                .constrainAs(horizontalRow) {
                    top.linkTo(attachMedia.bottom)
                    start.linkTo(parent.start)

                }
        ) {
            items(selectedImages) {
                ImageItem(uri = it)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 15.dp,
                end = 15.dp,
                bottom = 10.dp
            ),
        contentAlignment = Alignment.BottomEnd

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Anyone can reply & quote ",
                fontSize = 13.sp,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
            ElevatedButton(
                onClick = {
                    threadViewModel.uploadPost(thread, selectedImages)
                },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Text(
                    text = "Post",
                )
            }
        }
    }

    when (postUiState) {
        is UiState.Loading -> {
            ProgressDialog("Please wait...","Your patience is greatly appreciated, thank you!")
            isDialog = true
        }

        is UiState.Success -> {
            isDialog = false
            navHostController.navigate(Routes.BottomNav.route){
                popUpTo(navHostController.graph.id)
                launchSingleTop = true
            }
            Toast.makeText(context, "Upload thread", Toast.LENGTH_SHORT).show()

        }

        is UiState.Error -> {
            isDialog = false
            Toast.makeText(
                context,
                "Error: " + (postUiState as UiState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

        }

        is UiState.ValidationError -> {
            isDialog = false
            LaunchedEffect(postUiState) {
                val validation = postUiState as UiState.ValidationError
                Toast.makeText(context, validation.message, Toast.LENGTH_SHORT).show()
            }
        }

        else -> {}
    }

}

@Composable
fun ImageItem(uri: Uri) {
    Image(
        painter = rememberImagePainter(data = uri),
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .padding(
                start = 8.dp,
                top = 5.dp,
                bottom = 5.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable { /* Handle click if needed */ },
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun PreviewLayout() {
//    AddThreads()
}

@Composable
fun BasicTextFailedMe(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (value.isEmpty()) {
            Text(text = hint, color = Color.Gray, fontSize = 16.sp)
        }
        BasicTextField(
            value = value, onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(
                color = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

