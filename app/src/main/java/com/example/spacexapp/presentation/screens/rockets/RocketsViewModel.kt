package com.example.spacexapp.presentation.screens.rockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacexapp.domain.models.Rocket
import com.example.spacexapp.domain.usecase.GetRocketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketsViewModel @Inject constructor(
    private val getRocketsUseCase: GetRocketsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RocketsUiState())
    val uiState: StateFlow<RocketsUiState> = _uiState.asStateFlow()

    private val PAGE_SIZE = 1

    init {
        loadFirstPage()
    }

    fun loadFirstPage() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    rockets = emptyList(),
                    currentPage = 1,
                    hasReachedEnd = false
                )
            }
            loadRockets(1)
        }
    }

    fun loadNextPage() {
        if (_uiState.value.isLoading || _uiState.value.hasReachedEnd) return

        val nextPage = _uiState.value.currentPage + 1
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            loadRockets(nextPage)
        }
    }

    private suspend fun loadRockets(page: Int) {
        getRocketsUseCase(page, PAGE_SIZE).collect { result ->
            result.fold(
                onSuccess = { rocketsPage ->
                    _uiState.update { currentState ->
                        val updatedRockets = if (page == 1) {
                            rocketsPage.rockets
                        } else {
                            currentState.rockets + rocketsPage.rockets
                        }

                        currentState.copy(
                            rockets = updatedRockets,
                            isLoading = false,
                            isLoadingMore = false,
                            currentPage = rocketsPage.currentPage,
                            totalPages = rocketsPage.totalPages,
                            hasReachedEnd = !rocketsPage.hasNextPage,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = throwable.message ?: "알 수 없는 오류"
                        )
                    }
                }
            )
        }
    }
}

data class RocketsUiState(
    val rockets: List<Rocket> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val hasReachedEnd: Boolean = false
)