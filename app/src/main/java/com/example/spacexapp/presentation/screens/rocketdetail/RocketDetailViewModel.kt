package com.example.spacexapp.presentation.screens.rocketdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacexapp.domain.models.Rocket
import com.example.spacexapp.domain.usecase.GetRocketByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketDetailViewModel @Inject constructor(
    private val getRocketByIdUseCase: GetRocketByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RocketDetailUiState())
    val uiState: StateFlow<RocketDetailUiState> = _uiState.asStateFlow()

    fun loadRocketDetails(rocketId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getRocketByIdUseCase(rocketId).collect { result ->
                result.fold(
                    onSuccess = { rocket ->
                        _uiState.update {
                            it.copy(
                                rocket = rocket,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.message ?: "Unknown error"
                            )
                        }
                    }
                )
            }
        }
    }
}

data class RocketDetailUiState(
    val rocket: Rocket? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)