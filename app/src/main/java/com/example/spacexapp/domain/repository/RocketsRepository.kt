package com.example.spacexapp.domain.repository

import com.example.spacexapp.domain.models.Rocket
import kotlinx.coroutines.flow.Flow

interface RocketRepository {
    fun getRockets(page: Int, limit: Int): Flow<Result<RocketsPage>>
    fun getRocketById(rocketId: String): Flow<Result<Rocket>>
}

data class RocketsPage(
    val rockets: List<Rocket>,
    val currentPage: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)