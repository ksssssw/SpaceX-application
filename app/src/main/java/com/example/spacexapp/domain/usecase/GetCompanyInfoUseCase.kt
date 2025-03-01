package com.example.spacexapp.domain.usecase

import com.example.spacexapp.domain.models.CompanyInfo
import com.example.spacexapp.domain.repository.CompanyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCompanyInfoUseCase @Inject constructor(
    private val repository: CompanyRepository
) {
    operator fun invoke(): Flow<Result<CompanyInfo>> {
        return repository.getCompanyInfo()
    }
}