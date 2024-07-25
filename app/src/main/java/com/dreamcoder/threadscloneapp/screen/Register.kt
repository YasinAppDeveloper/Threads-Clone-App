@file:Suppress("UNUSED_EXPRESSION")

package com.dreamcoder.threads.screens

import CustomOutlinedTextField
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dreamcoder.threads.viewmodel.RegisterViewModel
import com.dreamcoder.threadscloneapp.R
import com.dreamcoder.threadscloneapp.components.CustomDefaultButton
import com.dreamcoder.threadscloneapp.components.CustomText
import com.dreamcoder.threadscloneapp.navigation.Routes
import com.dreamcoder.threadscloneapp.util.ProgressDialog


@SuppressLint("ShowToast")
@Composable
fun Register(
    navHostController: NavHostController, viewModel: RegisterViewModel = hiltViewModel()
) {

    val registerState by viewModel.registrationState.collectAsState()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isDialog by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val content = LocalContext.current

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else Manifest.permission.READ_EXTERNAL_STORAGE

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(content, "Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(content, "Permission is dined", Toast.LENGTH_SHORT).show()
            }
        }

    Surface() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp
                )
        ) {
            Column(
                modifier = Modifier.padding(top = 40.dp)
            ) {

                CustomText(
                    title = "Hey there,",
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    textSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                CustomText(
                    title = "Create Aa new account",
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    textSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )


                Image(
                    painter = if (imageUri == null) painterResource(id = R.drawable.placeholder)
                    else rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "profile",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            val isGranted = ContextCompat.checkSelfPermission(
                                content, permissionToRequest
                            ) == PackageManager.PERMISSION_GRANTED

                            if (isGranted) {
                                launcher.launch("image/*")
                            } else {
                                permissionLauncher.launch(permissionToRequest)
                            }
                        },
                    contentScale = ContentScale.Crop
                )

                CustomOutlinedTextField(
                    label = "Full Name",
                    text = name,
                    onTextChange = { name = it },
                    keyboardOptions = KeyboardOptions.Default
                )
                CustomOutlinedTextField(
                    label = "User Name",
                    text = username,
                    onTextChange = { username = it },
                    keyboardOptions = KeyboardOptions.Default
                )
                CustomOutlinedTextField(
                    label = "Bio",
                    text = bio,
                    onTextChange = { bio = it },
                    keyboardOptions = KeyboardOptions.Default
                )
                CustomOutlinedTextField(
                    label = "E-mail", text = email, onTextChange = { email = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    ),
                )

                CustomOutlinedTextField(label = "Password",
                    text = password,
                    onTextChange = { password = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                    ),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (isPasswordVisible) R.drawable.password_show else R.drawable.password_hide
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    })

                CustomDefaultButton(
                    title = "Register",
                    onclick = {
                        imageUri?.let {
                            viewModel.userRegister(
                                name, username, bio, email, password, it, content
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    textColor = Color.White,
                    fontFamily = FontFamily.SansSerif
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    CustomText(
                        title = "All ready have an account?",
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Serif,
                        textSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                    )

                    CustomText(title = " Login",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        textSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.clickable {
                            navHostController.navigate(Routes.Login.route)
                        })

                }

            }
        }
        when (registerState) {
            is UiState.Loading -> {
                ProgressDialog(
                    "Please wait...", "Your patience is greatly appreciated, thank you!"
                )
                isDialog = true
            }

            is UiState.Success -> {
                isDialog = false
                navHostController.navigate(Routes.BottomNav.route) {
                    popUpTo(navHostController.graph.id)
                    launchSingleTop = true
                }
                Toast.makeText(content, "Upload thread", Toast.LENGTH_SHORT).show()

            }

            is UiState.Error -> {
                isDialog = false
                Toast.makeText(
                    content,
                    "Error: " + (registerState as UiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()

            }

            is UiState.ValidationError -> {
                isDialog = false
                LaunchedEffect(registerState) {
                    val validation = registerState as UiState.ValidationError
                    Toast.makeText(content, validation.message, Toast.LENGTH_SHORT).show()
                    viewModel.restartViewModel()
                }
            }

            else -> {}
        }

    }
}

