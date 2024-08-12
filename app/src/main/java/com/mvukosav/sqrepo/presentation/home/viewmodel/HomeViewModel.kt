package com.mvukosav.sqrepo.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.usecases.GetRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getRepositoriesUseCase: GetRepositoriesUseCase
) : ViewModel() {
    val repoListFlow: Flow<PagingData<SquareRepo>> = getRepositoriesUseCase.createPager()
        .catch { emit(PagingData.empty()) }
        .cachedIn(viewModelScope)

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events

    fun onAction(action: HomeAction) {
        viewModelScope.launch {
            when (action) {
                is HomeAction.OnRepoClicked -> _events.emit(HomeEvent.NavigateToDetails(action.repoId))
            }
        }
    }
}
