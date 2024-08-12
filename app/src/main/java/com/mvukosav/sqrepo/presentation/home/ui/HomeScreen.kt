package com.mvukosav.sqrepo.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.mvukosav.sqrepo.R
import com.mvukosav.sqrepo.presentation.Screen
import com.mvukosav.sqrepo.presentation.home.viewmodel.HomeAction
import com.mvukosav.sqrepo.presentation.home.viewmodel.HomeEvent
import com.mvukosav.sqrepo.presentation.home.viewmodel.HomeViewModel
import com.mvukosav.sqrepo.presentation.ui.theme.dirtyWhite
import com.mvukosav.sqrepo.presentation.ui.theme.midnightNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val lazyPagingItems = viewModel.repoListFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToDetails -> {
                    navController.navigate(Screen.RepoDetailsScreen.route + "/${event.repoId}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.repo_repositories_title),
                        color = Color.White
                    )
                },
                colors = topAppBarColors(containerColor = midnightNavy)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(dirtyWhite)
        ) {
            if (lazyPagingItems.loadState.hasError) {
                val error = lazyPagingItems.loadState.refresh as LoadState.Error
                ErrorScreen(
                    message = stringResource(
                        R.string.repo_error_loading_more_data,
                        error.error.localizedMessage ?: ""
                    ),
                    onClick = { lazyPagingItems.retry() }
                )
            }

            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    LoadingIndicator()
                }

                else -> {
                    RepoList(lazyPagingItems, onRepoClick = { repoId ->
                        viewModel.onAction(HomeAction.OnRepoClicked(repoId))
                    })
                }
            }
        }
    }
}
