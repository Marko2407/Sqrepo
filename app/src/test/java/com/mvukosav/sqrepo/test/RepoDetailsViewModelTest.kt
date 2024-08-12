package com.mvukosav.sqrepo

import GetRepositoryUseCaseTest.Companion.TEST_REPOSITORY_ID
import androidx.lifecycle.SavedStateHandle
import com.mvukosav.sqrepo.common.Constants
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_LIMIT_EXCEED
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_NETWORK_ERROR
import com.mvukosav.sqrepo.common.Resource
import com.mvukosav.sqrepo.data.remote.dto.OwnerDto
import com.mvukosav.sqrepo.data.remote.dto.SquareRepositoryDto
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.repository.SquareRepository
import com.mvukosav.sqrepo.domain.usecases.GetRepositoryUseCase
import com.mvukosav.sqrepo.presentation.details.viewmodel.RepoDetailsAction
import com.mvukosav.sqrepo.presentation.details.viewmodel.RepoDetailsEvent
import com.mvukosav.sqrepo.presentation.details.viewmodel.RepoDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailsViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getRepositoryUseCase: GetRepositoryUseCase = mockk()
    private val repository = mockk<SquareRepository>(relaxed = true)
    private val savedStateHandle =
        SavedStateHandle(mapOf(Constants.PARAM_REPO_ID to TEST_REPOSITORY_ID.toString()))
    private lateinit var viewModel: RepoDetailsViewModel

    @Test
    fun `fetchRepositoryDetails updates state on success`() = runTest {
        val squareRepoDto = SquareRepositoryDto(
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
            owner = OwnerDto(avatarUrl = "")
        )
        val squareRepo = squareRepoDto.toDomain()

        coEvery { repository.getRepository(TEST_REPOSITORY_ID) } returns squareRepoDto
        coEvery { getRepositoryUseCase.invoke(TEST_REPOSITORY_ID) } returns flow {
            emit(
                Resource.Success(
                    squareRepo
                )
            )
        }
        viewModel = RepoDetailsViewModel(getRepositoryUseCase, savedStateHandle)
        advanceUntilIdle()

        assertEquals(squareRepo, viewModel.repositoryDetailsState.value.repoDetails)
        assertNull(viewModel.repositoryDetailsState.value.error)
        assertFalse(viewModel.repositoryDetailsState.value.isLoading)
    }

    @Test
    fun `fetchRepositoryDetails updates state on loading`() = runTest {
        val loadingFlow: Flow<Resource<SquareRepo?>> = flow { emit(Resource.Loading()) }
        coEvery { getRepositoryUseCase.invoke(TEST_REPOSITORY_ID) } returns loadingFlow
        viewModel = RepoDetailsViewModel(getRepositoryUseCase, savedStateHandle)
        advanceUntilIdle()

        assertTrue(viewModel.repositoryDetailsState.value.isLoading)
        assertNull(viewModel.repositoryDetailsState.value.repoDetails)
        assertNull(viewModel.repositoryDetailsState.value.error)
    }

    @Test
    fun `fetchRepositoryDetails updates state on error - HttpException`() = runTest {
        val errorFlow: Flow<Resource<SquareRepo?>> =
            flow { emit(Resource.Error(ERROR_MESSAGE_LIMIT_EXCEED)) }

        coEvery { getRepositoryUseCase.invoke(TEST_REPOSITORY_ID) } returns errorFlow

        viewModel = RepoDetailsViewModel(getRepositoryUseCase, savedStateHandle)

        advanceUntilIdle()

        assertEquals(ERROR_MESSAGE_LIMIT_EXCEED, viewModel.repositoryDetailsState.value.error)
        assertNull(viewModel.repositoryDetailsState.value.repoDetails)
        assertTrue(!viewModel.repositoryDetailsState.value.isLoading)
    }

    @Test
    fun `fetchRepositoryDetails updates state on error - IOException`() = runTest {
        val errorFlow: Flow<Resource<SquareRepo?>> =
            flow { emit(Resource.Error(ERROR_MESSAGE_NETWORK_ERROR)) }

        coEvery { getRepositoryUseCase.invoke(TEST_REPOSITORY_ID) } returns errorFlow

        viewModel = RepoDetailsViewModel(getRepositoryUseCase, savedStateHandle)

        advanceUntilIdle()

        assertEquals(ERROR_MESSAGE_NETWORK_ERROR, viewModel.repositoryDetailsState.value.error)
        assertNull(viewModel.repositoryDetailsState.value.repoDetails)
        assertTrue(!viewModel.repositoryDetailsState.value.isLoading)
    }

    @Test
    fun `onAction emits OpenBrowser event`() = runTest {
        val squareRepoDto = SquareRepositoryDto(
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
            owner = OwnerDto(avatarUrl = "")
        )
        val squareRepo = squareRepoDto.toDomain()

        coEvery { repository.getRepository(TEST_REPOSITORY_ID) } returns squareRepoDto

        coEvery { getRepositoryUseCase.invoke(TEST_REPOSITORY_ID) } returns flow {
            emit(
                Resource.Success(
                    squareRepo
                )
            )
        }

        viewModel = RepoDetailsViewModel(getRepositoryUseCase, savedStateHandle)

        val expectedUrl = TEST_URL
        val expectedEvent = RepoDetailsEvent.OpenBrowser(expectedUrl)

        val action = RepoDetailsAction.OnOpenRepositoryClick(expectedUrl)
        viewModel.onAction(action)

        val emittedEvent = viewModel.events.first()
        assertEquals(expectedEvent, emittedEvent)
    }

    companion object {
        const val TEST_URL = "http://dummy.com"
    }
}
