package com.example.spacexapp.domain.usecase

import com.example.spacexapp.domain.models.Rocket
import com.example.spacexapp.domain.repository.RocketRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRocketByIdUseCase @Inject constructor(
    private val rocketRepository: RocketRepository
) {
    operator fun invoke(rocketId: String): Flow<Result<Rocket>> {
        return rocketRepository.getRocketById(rocketId)
    }
}