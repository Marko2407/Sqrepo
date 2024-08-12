package com.mvukosav.sqrepo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mvukosav.sqrepo.common.Constants.PARAM_REPO_ID
import com.mvukosav.sqrepo.presentation.details.viewmodel.ui.RepoDetailsScreen
import com.mvukosav.sqrepo.presentation.home.ui.HomeScreen
import com.mvukosav.sqrepo.presentation.ui.theme.SqrepoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SqrepoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(
                            route = Screen.HomeScreen.route
                        ) {
                            HomeScreen(navController = navController)
                        }

                        composable(route = Screen.RepoDetailsScreen.route + "/{$PARAM_REPO_ID}") {
                            RepoDetailsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
