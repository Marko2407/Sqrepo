package com.mvukosav.sqrepo.presentation.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mvukosav.sqrepo.R
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.presentation.ui.theme.midnightNavy


@Composable
fun RepoDetailsContent(repo: SquareRepo, modifier: Modifier = Modifier, onClickOpenBrowser:(String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = repo.avatarUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        placeholder(R.drawable.sqrepo_icon)
                        error(R.drawable.sqrepo_icon)
                    }).build()
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )
            }

            Column {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.DarkGray
                )
                Text(
                    text = repo.visibility,
                    style = MaterialTheme.typography.labelMedium,
                    color = midnightNavy
                )
            }
        }

        Text(
            text = repo.description,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            color = Color.DarkGray
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.repo_forks, repo.forks),
                style = MaterialTheme.typography.labelLarge,
                color = Color.DarkGray
            )
            Text(
                text = stringResource(R.string.repo_watchers, repo.watchers),
                style = MaterialTheme.typography.labelLarge,
                color = Color.DarkGray
            )
            Text(
                text = stringResource(R.string.repo_issues, repo.openIssues),
                style = MaterialTheme.typography.labelLarge,
                color = Color.DarkGray
            )
        }

        ClickableText(
            text = AnnotatedString(stringResource(R.string.repo_open_repository)),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = midnightNavy,
                textDecoration = TextDecoration.Underline
            ),
            onClick = { onClickOpenBrowser(repo.htmlUrl) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RepoDetailsContentPreview() {
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
            RepoDetailsContent(
                repo = mockRepos,
                onClickOpenBrowser = {}
            )
        }
    }
}
