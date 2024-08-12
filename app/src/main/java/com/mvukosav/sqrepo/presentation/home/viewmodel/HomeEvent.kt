package com.mvukosav.sqrepo.presentation.home.viewmodel

sealed interface HomeEvent {
    data class NavigateToDetails(val repoId: Int) : HomeEvent
}
