package com.mvukosav.sqrepo.domain.repository

import com.mvukosav.sqrepo.data.remote.dto.SquareRepositoryDto

interface SquareRepository {

    suspend fun getRepositories(page: Int, perPage: Int): List<SquareRepositoryDto>
    suspend fun getRepository(id: Int): SquareRepositoryDto?
}
