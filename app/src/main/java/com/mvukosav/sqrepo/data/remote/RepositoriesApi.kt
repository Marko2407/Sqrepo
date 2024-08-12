package com.mvukosav.sqrepo.data.remote

import com.mvukosav.sqrepo.data.remote.dto.SquareRepositoryDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoriesApi {

    @GET("/orgs/square/repos")
    suspend fun getRepositories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<SquareRepositoryDto>
}
