package com.mvukosav.sqrepo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mvukosav.sqrepo.common.Constants
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.repository.RepoPagingSourceFactory
import com.mvukosav.sqrepo.domain.repository.SquareRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepoPagingSource @Inject constructor(
    private val repository: SquareRepository
) : PagingSource<Int, SquareRepo>(), RepoPagingSourceFactory {

    override fun create(): PagingSource<Int, SquareRepo> {
        return this
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SquareRepo> {
        val page = params.key ?: 1
        val loadSize = params.loadSize
        return try {
            val response = repository.getRepositories(page, loadSize).map { it.toDomain() }
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: HttpException) {
            if (e.code() == Constants.HTTP_CODE_FORBIDDEN) {
                LoadResult.Error(Exception("${e.code()} ${Constants.ERROR_MESSAGE_LIMIT_EXCEED}"))
            } else {
                LoadResult.Error(Exception(e.localizedMessage))
            }
        } catch (e: IOException) {
            LoadResult.Error(Exception(Constants.ERROR_MESSAGE_NETWORK_ERROR))
        } catch (e: Exception) {
            LoadResult.Error(Exception(Constants.ERROR_MESSAGE_UNKNOWN_ERROR))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SquareRepo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.pages
            .firstOrNull { it.data.contains(state.closestItemToPosition(anchorPosition)) }
            ?: return null
        return closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
    }
}
