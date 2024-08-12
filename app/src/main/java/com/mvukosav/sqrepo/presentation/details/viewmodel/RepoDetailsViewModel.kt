package com.mvukosav.sqrepo.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.sqrepo.common.Constants
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_NOT_FOUND
import com.mvukosav.sqrepo.common.Constants.ERROR_MESSAGE_UNKNOWN_ERROR
import com.mvukosav.sqrepo.common.Resource
import com.mvukosav.sqrepo.domain.model.SquareRepo
import com.mvukosav.sqrepo.domain.usecases.GetRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val getRepositoryUseCase: GetRepositoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _repositoryDetailsState = MutableStateFlow(RepositoryDetailsState())
    val repositoryDetailsState: StateFlow<RepositoryDetailsState> = _repositoryDetailsState

    private val _events = MutableSharedFlow<RepoDetailsEvent>()
    val events: SharedFlow<RepoDetailsEvent> = _events

    init {
        val repositoryId: String? = savedStateHandle[Constants.PARAM_REPO_ID]
        repositoryId?.let { id ->
            fetchRepositoryDetails(id.toInt())
        } ?: run {
            _repositoryDetailsState.value = RepositoryDetailsState(error = ERROR_MESSAGE_NOT_FOUND)
        }
    }

    fun onAction(action: RepoDetailsAction) {
        viewModelScope.launch {
            when (action) {
                RepoDetailsAction.OnBackClick -> _events.emit(RepoDetailsEvent.NavigateBack)
                is RepoDetailsAction.OnOpenRepositoryClick -> _events.emit(
                    RepoDetailsEvent.OpenBrowser(
                        action.url
                    )
                )
            }
        }
    }

    private fun fetchRepositoryDetails(id: Int) {
        getRepositoryUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _repositoryDetailsState.value = RepositoryDetailsState(
                    error = result.message ?: ERROR_MESSAGE_UNKNOWN_ERROR
                )

                is Resource.Loading -> _repositoryDetailsState.value = RepositoryDetailsState(
                    isLoading = true
                )

                is Resource.Success -> _repositoryDetailsState.value = RepositoryDetailsState(
                    repoDetails = result.data
                )
            }
        }.launchIn(viewModelScope)
    }
}


data class RepositoryDetailsState(
    val isLoading: Boolean = false,
    val repoDetails: SquareRepo? = null,
    val error: String? = null
)
