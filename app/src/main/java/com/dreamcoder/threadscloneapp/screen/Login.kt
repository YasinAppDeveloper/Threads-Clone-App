package com.dreamcoder.threadscloneapp.screen

import CustomOutlinedTextField
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.dreamcoder.threads.viewmodel.LoginViewModel
import com.dreamcoder.threadscloneapp.R
import com.dreamcoder.threadscloneapp.components.CustomDefaultButton
import com.dreamcoder.threadscloneapp.components.CustomText
import com.dreamcoder.threadscloneapp.navigation.Routes
import com.dreamcoder.threadscloneapp.util.ProgressDialog

@Composable
fun Login(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
    ) {

    val content = LocalContext.current
    val loginUiSate by loginViewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 120.dp)
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
            title = "Welcome Back",
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.SansSerif,
            textSize = 18.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        CustomOutlinedTextField(
            label = "Email",
            text = email,
            onTextChange = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        CustomOutlinedTextField(
            label = "Password",
            text = password,
            onTextChange = { password = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
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
            }
        )

        CustomDefaultButton(
            title = "Login",
            onclick = {
                loginViewModel.userLogin(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            textColor = Color.White,
            fontFamily = FontFamily.SansSerif
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            CustomText(
                title = "Don't have account?",
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Serif,
                textSize = 12.sp,
                color = Color.Black,
                modifier = Modifier
            )

            CustomText(
                title = " Register",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.clickable {
                    navHostController.navigate(Routes.Register.route)
                }
            )
        }
    }

    when (loginUiSate) {
        is UiState.Loading -> {
            ProgressDialog("Please Wait...","Find the user and than login....")
            isDialog = true
        }

        is UiState.Success -> {
            isDialog = false
            Toast.makeText(content, "User Is Register", Toast.LENGTH_SHORT).show()

        }

        is UiState.Error -> {
            isDialog = false
            Toast.makeText(
                content,
                "Error: " + (loginUiSate as UiState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

        }

        is UiState.ValidationError -> {
            isDialog = false
            LaunchedEffect(loginUiSate) {
                val validation = loginUiSate as UiState.ValidationError
                Toast.makeText(content, validation.message, Toast.LENGTH_SHORT).show()
                loginViewModel.restartViewModel()
            }
        }

        else -> {}
    }

}