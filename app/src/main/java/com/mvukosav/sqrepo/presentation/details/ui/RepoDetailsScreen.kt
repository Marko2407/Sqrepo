package com.mvukosav.sqrepo.presentation.details.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mvukosav.sqrepo.R
import com.mvukosav.sqrepo.presentation.details.viewmodel.RepoDetailsAction
import com.mvukosav.sqrepo.presentation.details.viewmodel.RepoDetailsEvent
import com.mvukosav.sqrepo.presentation.details.viewmodel.RepoDetailsViewModel
import com.mvukosav.sqrepo.presentation.ui.theme.dirtyWhite
import com.mvukosav.sqrepo.presentation.ui.theme.midnightNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    navController: NavController,
    viewModel: RepoDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.repositoryDetailsState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                RepoDetailsEvent.NavigateBack -> navController.popBackStack()
                is RepoDetailsEvent.OpenBrowser -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                    context.startActivity(intent)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.repo_repository_details_title),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onAction(RepoDetailsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = topAppBarColors(containerColor = midnightNavy)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(dirtyWhite)
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                if (state.value.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (state.value.error != null) {
                    state.value.error?.let { error -> RepoDetailsErrorScreen(error) }
                } else {
                    state.value.repoDetails?.let {
                        RepoDetailsContent(
                            repo = it,
                            onClickOpenBrowser = { url ->
                                viewModel.onAction(
                                    RepoDetailsAction.OnOpenRepositoryClick(url)
                                )
                            },
                        )
                    }
                }
            }
        }
    )
}
