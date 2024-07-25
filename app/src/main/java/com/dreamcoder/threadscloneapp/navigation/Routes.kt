package com.dreamcoder.threadscloneapp.navigation

sealed class Routes(
    val route:String
) {
    object Home : Routes("Home")
    object Search : Routes("Search")
    object AddThreads : Routes("AddThreads")
    object Notification : Routes("Notification")
    object Profile : Routes("Profile")
    object Splash : Routes("Splash")
    object BottomNav : Routes("bottomNav")
    object Register : Routes("Register")
    object Login : Routes("Login")
}