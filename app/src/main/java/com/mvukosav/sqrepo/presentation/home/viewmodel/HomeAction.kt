package com.mvukosav.sqrepo.presentation.home.viewmodel

sealed interface HomeAction {
    data class OnRepoClicked(val repoId: Int) : HomeAction
}
