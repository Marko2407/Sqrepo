package com.mvukosav.sqrepo.domain.repository

import androidx.paging.PagingSource
import com.mvukosav.sqrepo.domain.model.SquareRepo

interface RepoPagingSourceFactory {
    fun create(): PagingSource<Int, SquareRepo>
}
