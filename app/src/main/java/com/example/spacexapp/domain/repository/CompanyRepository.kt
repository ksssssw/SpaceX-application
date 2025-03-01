package com.example.spacexapp.domain.repository

import com.example.spacexapp.domain.models.CompanyInfo
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    fun getCompanyInfo(): Flow<Result<CompanyInfo>>
}