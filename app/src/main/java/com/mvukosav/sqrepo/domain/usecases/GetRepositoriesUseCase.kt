package com.mvukosav.sqrepo.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.repository.RepoPagingSourceFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepositoriesUseCase @Inject constructor(
    private val repoPagingSourceFactory: RepoPagingSourceFactory
) {
    fun createPager(): Flow<PagingData<SquareRepo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = { repoPagingSourceFactory.create() }
        ).flow
    }
}

private const val PAGE_SIZE = 20
private const val INITIAL_LOAD_SIZE = 40
