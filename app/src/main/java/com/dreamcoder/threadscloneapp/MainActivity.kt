package com.dreamcoder.threadscloneapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dreamcoder.threadscloneapp.navigation.NavigationGraph
import com.dreamcoder.threadscloneapp.ui.theme.ThreadsCloneAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThreadsCloneAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {

                    navHostController = rememberNavController()
                    NavigationGraph(navHostController = navHostController)

                }
            }
        }
    }
}

