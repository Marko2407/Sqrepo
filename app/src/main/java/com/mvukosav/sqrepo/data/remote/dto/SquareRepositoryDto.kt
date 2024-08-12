package com.mvukosav.sqrepo.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mvukosav.sqrepo.domain.model.SquareRepo

data class SquareRepositoryDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("html_url")
    val htmlUrl: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("watchers")
    val watchers: Int?,
    @SerializedName("forks")
    val forks: Int?,
    @SerializedName("open_issues")
    val openIssues: Int?,
    @SerializedName("visibility")
    val visibility: String?,
    @SerializedName("owner")
    val owner: OwnerDto?
) {
    fun toDomain(): SquareRepo = SquareRepo(
        id = id,
        name = name.orEmpty(),
        htmlUrl = htmlUrl.orEmpty(),
        description = description.orEmpty(),
        createdAt = createdAt.orEmpty(),
        updatedAt = updatedAt.orEmpty(),
        watchers = watchers ?: 0,
        forks = forks ?: 0,
        openIssues = openIssues ?: 0,
        visibility = visibility.orEmpty(),
        avatarUrl = owner?.avatarUrl.orEmpty()
    )
}


data class OwnerDto(
    @SerializedName("avatar_url")
    val avatarUrl: String?
)
