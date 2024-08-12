package com.mvukosav.sqrepo.presentation.home.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.presentation.Screen


@Composable
fun RepoList(lazyPagingItems: LazyPagingItems<SquareRepo>, onRepoClick: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.padding(vertical = 20.dp)) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { repo ->
                RepoListItem(
                    repo = repo,
                    onClick = { onRepoClick(repo.id) }
                )
            }
        }
        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {
                LoadingIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoListPreview() {
    val navController = rememberNavController()
    val mockRepos = List(10) { index ->
        SquareRepo(
            id = index,
            name = "Repo $index",
            description = "Description for Repo $index",
            htmlUrl = "https://github.com/repo$index",
            watchers = 100 + index,
            forks = 10 + index,
            visibility = "Open",
            avatarUrl = "",
            createdAt = "",
            updatedAt = "",
            openIssues = 2
        )
    }

    LazyColumn(modifier = Modifier.padding(vertical = 20.dp)) {
        items(mockRepos) { repo ->
            RepoListItem(
                repo = repo,
                onClick = { navController.navigate(Screen.RepoDetailsScreen.route + "/${repo.id}") }
            )
        }
    }
}
