package com.mvukosav.sqrepo.test

import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_NETWORK_ERROR
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_NOT_FOUND
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_UNKNOWN_ERROR
import com.mvukosav.sqrepo.common.Constants.HTTP_CODE_FORBIDDEN
import com.mvukosav.sqrepo.common.Resource
import com.mvukosav.sqrepo.data.remote.dto.SquareRepositoryDto
import com.mvukosav.sqrepo.domain.repository.SquareRepository
import com.mvukosav.sqrepo.domain.usecases.GetRepositoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GetRepositoryUseCaseTest {

    private val repository = mockk<SquareRepository>(relaxed = true)
    private val useCase = GetRepositoryUseCase(repository)

    @Test
    fun `invoke emits Loading, then Success when repository returns non-null`() = runTest {
        val expectedRepo = SquareRepositoryDto(
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
            owner = null
        )
        coEvery { repository.getRepository(TEST_REPOSITORY_ID) } returns expectedRepo
        val results = useCase(TEST_REPOSITORY_ID).toList()

        assertTrue(results[LOADING_STATE] is Resource.Loading)
        assertTrue(results[RESPONSE] is Resource.Success)
        assertEquals(expectedRepo.toDomain(), (results[RESPONSE] as Resource.Success).data)
    }

    @Test
    fun `invoke emits Loading, then Error when repository returns null`() = runTest {
        coEvery { repository.getRepository(TEST_REPOSITORY_ID) } returns null
        val results = useCase(TEST_REPOSITORY_ID).toList()

        assertTrue(results[LOADING_STATE] is Resource.Loading)
        assertTrue(results[RESPONSE] is Resource.Error)
        assertEquals(ERROR_MESSAGE_NOT_FOUND, (results[RESPONSE] as Resource.Error).message)
    }

    @Test
    fun `invoke emits Loading, then Error when HttpException is thrown`() = runTest {
        val mockResponse: Response<Any> = Response.error(
            HTTP_CODE_FORBIDDEN,
            ERROR_MESSAGE_UNKNOWN_ERROR.toResponseBody(null)
        )
        coEvery { repository.getRepository(TEST_REPOSITORY_ID) } throws HttpException(mockResponse)
        val results = useCase(TEST_REPOSITORY_ID).toList()

        assertTrue(results[LOADING_STATE] is Resource.Loading)
        assertTrue(results[RESPONSE] is Resource.Error)
        assertEquals(
            HTTP_ERROR_MESSAGE,
            (results[RESPONSE] as Resource.Error).message
        )
    }

    @Test
    fun `invoke emits Loading, then Error when IOException is thrown`() = runTest {
        coEvery { repository.getRepository(TEST_REPOSITORY_ID) } throws IOException()
        val results = useCase(TEST_REPOSITORY_ID).toList()

        assertTrue(results[LOADING_STATE] is Resource.Loading)
        assertTrue(results[RESPONSE] is Resource.Error)
        assertEquals(ERROR_MESSAGE_NETWORK_ERROR, (results[RESPONSE] as Resource.Error).message)
    }

    companion object {
        const val TEST_REPOSITORY_ID = 1
        private const val LOADING_STATE = 0
        private const val RESPONSE = 1
        private const val HTTP_ERROR_MESSAGE = "HTTP $HTTP_CODE_FORBIDDEN Response.error()"
    }
}
