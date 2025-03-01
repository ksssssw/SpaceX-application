package com.example.spacexapp.presentation.screens.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacexapp.domain.models.CompanyInfo
import com.example.spacexapp.domain.usecase.GetCompanyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getCompanyInfoUseCase: GetCompanyInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()

    init {
        loadCompanyInfo()
    }

    fun loadCompanyInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCompanyInfoUseCase().collect { result ->
                result.fold(
                    onSuccess = { companyInfo ->
                        _uiState.update {
                            it.copy(
                                companyInfo = companyInfo,
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

data class InfoUiState(
    val companyInfo: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)