package com.dreamcoder.threadscloneapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dreamcoder.threads.screens.MyBottomNavigation
import com.dreamcoder.threads.screens.Register
import com.dreamcoder.threads.viewmodel.RegisterViewModel
import com.dreamcoder.threads.viewmodel.UploadThreadViewModel
import com.dreamcoder.threadscloneapp.mvvm.FetchPostedThreadImages
import com.dreamcoder.threadscloneapp.screen.AddThreads
import com.dreamcoder.threadscloneapp.screen.Home
import com.dreamcoder.threadscloneapp.screen.Login
import com.dreamcoder.threadscloneapp.screen.Notification
import com.dreamcoder.threadscloneapp.screen.Profile
import com.dreamcoder.threadscloneapp.screen.Search
import com.dreamcoder.threadscloneapp.screen.SplashScreen


@Composable
fun NavigationGraph(navHostController: NavHostController) {
    val registerViewModel = hiltViewModel<RegisterViewModel>()
    val uploadThreadViewModel = hiltViewModel<UploadThreadViewModel>()
    val fetchPostedImageViewModel = hiltViewModel<FetchPostedThreadImages>()
    NavHost(
        navController = navHostController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
           SplashScreen(navHostController = navHostController)
        }
        composable(Routes.Home.route) {
           Home()
        }
        composable(Routes.Search.route) {
           Search()
        }
        composable(Routes.AddThreads.route) {
           AddThreads(uploadThreadViewModel,navHostController)
        }
        composable(Routes.Notification.route) {
            Notification()
        }
        composable(Routes.Profile.route) {
            Profile(fetchPostedImageViewModel,navHostController)
        }
        composable(Routes.BottomNav.route) {
            MyBottomNavigation(navHostController)
        }
        composable(Routes.Register.route) {
            Register(navHostController,registerViewModel)
        }
        composable(Routes.Login.route) {
            Login(navHostController)
        }
    }
}
