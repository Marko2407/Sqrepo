package com.mvukosav.sqrepo.presentation.details.viewmodel

sealed interface RepoDetailsAction {
    data object OnBackClick : RepoDetailsAction
    data class OnOpenRepositoryClick(val url: String) : RepoDetailsAction
}
