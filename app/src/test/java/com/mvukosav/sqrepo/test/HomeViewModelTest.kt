package com.mvukosav.sqrepo.test

import androidx.paging.PagingData
import androidx.paging.map
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_UNKNOWN_ERROR
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.usecases.GetRepositoriesUseCase
import com.mvukosav.sqrepo.presentation.home.viewmodel.HomeAction
import com.mvukosav.sqrepo.presentation.home.viewmodel.HomeEvent
import com.mvukosav.sqrepo.presentation.home.viewmodel.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getRepositoriesUseCase: GetRepositoriesUseCase = mockk()
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `observeRepoList updates state on success`() = runTest {
        val squareRepo = SquareRepo(
            id = 1,
            name = "Repo1",
            htmlUrl = "http://dummy.com",
            description = "Description",
            createdAt = "2024-08-08T00:00:00Z",
            updatedAt = "2024-08-08T01:00:00Z",
            watchers = 10,
            forks = 5,
            openIssues = 2,
            visibility = "public",
            avatarUrl = ""
        )
        val pagingData = PagingData.from(listOf(squareRepo))
        val flow: Flow<PagingData<SquareRepo>> = flowOf(pagingData)

        coEvery { getRepositoriesUseCase.createPager() } returns flow

        homeViewModel = HomeViewModel(getRepositoriesUseCase)

        val job = launch {
            homeViewModel.repoListFlow.onEach { result ->
                assertEquals(listOf(squareRepo), result.map { repo -> repo })
            }
        }
        advanceUntilIdle()

        job.cancelAndJoin()
    }

    @Test
    fun `repoListFlow handles errors correctly`() = runTest {
        val errorMessage = ERROR_MESSAGE_UNKNOWN_ERROR
        coEvery { getRepositoriesUseCase.createPager() } returns flow {
            throw RuntimeException(
                errorMessage
            )
        }
        homeViewModel = HomeViewModel(getRepositoriesUseCase)
        val job = launch {
            homeViewModel.repoListFlow.catch { error ->
                assertEquals(errorMessage, error)
            }
        }

        advanceUntilIdle()
        job.cancelAndJoin()
    }

    @Test
    fun `onAction emits NavigateToDetails action`() = runTest {
        val squareRepo = SquareRepo(
            id = 1,
            name = "Repo1",
            htmlUrl = "http://dummy.com",
            description = "Description",
            createdAt = "2024-08-08T00:00:00Z",
            updatedAt = "2024-08-08T01:00:00Z",
            watchers = 10,
            forks = 5,
            openIssues = 2,
            visibility = "public",
            avatarUrl = ""
        )
        val pagingData = PagingData.from(listOf(squareRepo))
        val flow: Flow<PagingData<SquareRepo>> = flowOf(pagingData)

        coEvery { getRepositoriesUseCase.createPager() } returns flow

        homeViewModel = HomeViewModel(getRepositoriesUseCase)

        val expectedRepoId = squareRepo.id
        val expectedEvent = HomeEvent.NavigateToDetails(expectedRepoId)

        val action = HomeAction.OnRepoClicked(expectedRepoId)
        homeViewModel.onAction(action)

        val emittedEvent = homeViewModel.events.first()
        assertEquals(expectedEvent, emittedEvent)
    }

}
