package com.example.spacexapp.domain.usecase

import com.example.spacexapp.domain.repository.RocketRepository
import com.example.spacexapp.domain.repository.RocketsPage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRocketsUseCase @Inject constructor(
    private val rocketsRepository: RocketRepository
) {
    operator fun invoke(page: Int, limit: Int): Flow<Result<RocketsPage>> {
        return rocketsRepository.getRockets(page, limit)
    }
}