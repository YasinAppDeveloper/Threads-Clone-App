package com.dreamcoder.threads.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dreamcoder.threads.viewmodel.UploadThreadViewModel
import com.dreamcoder.threadscloneapp.model.BottomNavData
import com.dreamcoder.threadscloneapp.mvvm.FetchPostedThreadImages
import com.dreamcoder.threadscloneapp.navigation.Routes
import com.dreamcoder.threadscloneapp.screen.AddThreads
import com.dreamcoder.threadscloneapp.screen.Home
import com.dreamcoder.threadscloneapp.screen.Notification
import com.dreamcoder.threadscloneapp.screen.Profile
import com.dreamcoder.threadscloneapp.screen.Search

@Composable
fun MyBottomNavigation(
    navHostController: NavHostController,
    threadViewModel : UploadThreadViewModel = hiltViewModel(),
    postedThreadImages: FetchPostedThreadImages = hiltViewModel()
) {

    val navHostController1 = rememberNavController()

    Scaffold(bottomBar = { MyBottomAppBar(navHostController1) }) { innerPadding ->
        NavHost(
            navController = navHostController1, startDestination = Routes.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home.route) {
                Home()
            }
            composable(Routes.Search.route) {
                Search()
            }
            composable(Routes.AddThreads.route) {
                AddThreads(threadViewModel,navHostController)
            }
            composable(Routes.Notification.route) {
                Notification()
            }
            composable(Routes.Profile.route) {
                Profile(postedThreadImages,navHostController)
            }
        }
    }
}

@Composable
fun MyBottomAppBar(navHostController1: NavHostController) {

    val backStackEntry = navHostController1.currentBackStackEntryAsState()

    val bottomList = listOf(
        BottomNavData(
            "Home", Routes.Home.route, Icons.Rounded.Home
        ), BottomNavData(
            "Search", Routes.Search.route, Icons.Rounded.Search
        ), BottomNavData(
            "Add Threads", Routes.AddThreads.route, Icons.Rounded.Add
        ), BottomNavData(
            "Notification", Routes.Notification.route, Icons.Rounded.Notifications
        ), BottomNavData(
            "Profile", Routes.Profile.route, Icons.Rounded.AccountCircle
        )
    )

    BottomAppBar(
        containerColor = Color.White,

        ) {
        bottomList.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(selected = selected, onClick = {
                navHostController1.navigate(it.route) {
                    popUpTo(navHostController1.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }, icon = {
                Icon(imageVector = it.icon, contentDescription = it.title)
            })
        }
    }

}
