package com.dreamcoder.threadscloneapp.screen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dreamcoder.threadscloneapp.R
import com.dreamcoder.threadscloneapp.navigation.Routes
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.size(90.dp)
        )
    }
    Handler(Looper.getMainLooper()).postDelayed({
        if (auth.currentUser != null) {
            navHostController.navigate(Routes.BottomNav.route) {
                popUpTo(0)

            }
        } else {
            navHostController.navigate(Routes.Register.route) {
                popUpTo(0)
            }
        }
    }, 3000)
}