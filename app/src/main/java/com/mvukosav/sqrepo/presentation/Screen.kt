package com.mvukosav.sqrepo.presentation

import com.mvukosav.sqrepo.common.Constants.HOME_SCREEN
import com.mvukosav.sqrepo.common.Constants.REPO_DETAILS_SCREEN

sealed class Screen(val route: String) {
    data object HomeScreen : Screen(HOME_SCREEN)
    data object RepoDetailsScreen : Screen(REPO_DETAILS_SCREEN)
}
