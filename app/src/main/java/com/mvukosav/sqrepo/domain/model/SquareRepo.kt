package com.mvukosav.sqrepo.domain.model

data class SquareRepo(
    val id: Int,
    val name: String,
    val htmlUrl: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val watchers: Int,
    val forks: Int,
    val openIssues: Int,
    val visibility: String,
    val avatarUrl: String
)
