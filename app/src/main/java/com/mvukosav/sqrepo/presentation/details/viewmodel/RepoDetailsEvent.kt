package com.mvukosav.sqrepo.presentation.details.viewmodel

sealed interface RepoDetailsEvent {
    data object NavigateBack : RepoDetailsEvent
    data class OpenBrowser(val url: String) : RepoDetailsEvent
}
