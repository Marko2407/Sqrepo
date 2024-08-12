package com.mvukosav.sqrepo.presentation.home.viewmodel.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.mvukosav.sqrepo.R
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.presentation.Screen


@Composable
fun RepoListItem(
    repo: SquareRepo,
    onClick: (SquareRepo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(repo) },
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = repo.description,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.repo_forks, repo.forks),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = stringResource(R.string.repo_watchers, repo.watchers),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = stringResource(R.string.repo_issues, repo.openIssues),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoListItemPreview() {
    val navController = rememberNavController()
    val mockRepos =
        SquareRepo(
            id = 1,
            name = "Repo 1",
            description = "Description for Repo 1",
            htmlUrl = "https://github.com/repo1",
            watchers = 100 + 1,
            forks = 10 + 1,
            visibility = "Open",
            avatarUrl = "",
            createdAt = "",
            updatedAt = "",
            openIssues = 2
        )

    LazyColumn(modifier = Modifier.padding(vertical = 20.dp)) {
        item {
            RepoListItem(
                repo = mockRepos,
                onClick = { navController.navigate(Screen.RepoDetailsScreen.route + "/${mockRepos.id}") }
            )
        }
    }
}
