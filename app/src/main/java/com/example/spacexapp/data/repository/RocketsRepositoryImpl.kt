package com.example.spacexapp.data.repository

import com.example.spacexapp.data.api.SpaceXApi
import com.example.spacexapp.data.models.QueryOptions
import com.example.spacexapp.data.models.RocketQueryRequest
import com.example.spacexapp.data.models.RocketsResponse
import com.example.spacexapp.domain.models.Rocket
import com.example.spacexapp.domain.repository.RocketRepository
import com.example.spacexapp.domain.repository.RocketsPage
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RocketRepositoryImpl @Inject constructor(
    private val api: SpaceXApi
) : RocketRepository {

    override fun getRockets(page: Int, limit: Int): Flow<Result<RocketsPage>> = flow {
        try {
            val queryRequest = RocketQueryRequest(
                options = QueryOptions(
                    page = page,
                    limit = limit
                )
            )

            val response = api.getRocketsPaginated(queryRequest)

            val rocketsPage = RocketsPage(
                rockets = response.docs.map { it.toDomainModel() },
                currentPage = response.page,
                totalPages = response.totalPages,
                hasNextPage = response.hasNextPage
            )

            emit(Result.success(rocketsPage))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private fun RocketsResponse.toDomainModel(): Rocket {
        return Rocket(
            id = id,
            name = name,
            description = description,
            firstFlight = firstFlight,
            active = active,
            stages = stages,
            boosters = boosters,
            costPerLaunch = costPerLaunch,
            successRatePct = successRatePct,
            height = height.meters,
            diameter = diameter.meters,
            mass = mass.kg,
            images = flickrImages
        )
    }
}