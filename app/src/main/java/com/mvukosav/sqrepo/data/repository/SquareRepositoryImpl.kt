package com.mvukosav.sqrepo.data.repository

import com.mvukosav.sqrepo.data.remote.RepositoriesApi
import com.mvukosav.sqrepo.data.remote.dto.SquareRepositoryDto
import com.mvukosav.sqrepo.domain.repository.SquareRepository
import javax.inject.Inject

class SquareRepositoryImpl @Inject constructor(private val api: RepositoriesApi) :
    SquareRepository {

    private val cache = mutableMapOf<Int, SquareRepositoryDto>()

    override suspend fun getRepositories(page: Int, perPage: Int): List<SquareRepositoryDto> {
        val repositories = api.getRepositories(page = page, perPage = perPage)
        repositories.forEach { repo ->
            cache[repo.id] = repo
        }
        return repositories
    }

    override suspend fun getRepository(id: Int): SquareRepositoryDto? = cache[id]
}
