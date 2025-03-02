package com.example.spacexapp.di

import com.example.spacexapp.data.repository.CompanyRepositoryImpl
import com.example.spacexapp.data.repository.RocketRepositoryImpl
import com.example.spacexapp.domain.repository.CompanyRepository
import com.example.spacexapp.domain.repository.RocketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyRepository(
        companyRepositoryImpl: CompanyRepositoryImpl
    ): CompanyRepository

    @Binds
    @Singleton
    abstract fun bindRocketRepository(
        rocketRepositoryImpl: RocketRepositoryImpl
    ): RocketRepository
}