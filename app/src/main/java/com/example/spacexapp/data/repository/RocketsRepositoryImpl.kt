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

    // Cache for rockets to avoid unnecessary network calls
    private val rocketCache = mutableMapOf<String, Rocket>()

    override fun getRockets(page: Int, limit: Int): Flow<Result<RocketsPage>> = flow {
        try {
            val queryRequest = RocketQueryRequest(
                options = QueryOptions(
                    page = page,
                    limit = limit
                )
            )

            val response = api.getRocketsPaginated(queryRequest)

            val rockets = response.docs.map { it.toDomainModel() }

            // Update cache with fetched rockets
            rockets.forEach { rocket ->
                rocketCache[rocket.id] = rocket
            }

            val rocketsPage = RocketsPage(
                rockets = rockets,
                currentPage = response.page,
                totalPages = response.totalPages,
                hasNextPage = response.hasNextPage
            )

            emit(Result.success(rocketsPage))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getRocketById(rocketId: String): Flow<Result<Rocket>> = flow {
        try {
            // First check if the rocket is in the cache
            val cachedRocket = rocketCache[rocketId]
            if (cachedRocket != null) {
                emit(Result.success(cachedRocket))
                return@flow
            }

            // If not in cache, fetch it from API
            // Note: SpaceX API might not have a direct endpoint for single rocket,
            // so we may need to query with a filter or update the API interface
            val queryRequest = RocketQueryRequest(
                query = mapOf("_id" to rocketId),
                options = QueryOptions(page = 1, limit = 1)
            )

            val response = api.getRocketsPaginated(queryRequest)

            if (response.docs.isEmpty()) {
                emit(Result.failure(NoSuchElementException("Rocket not found")))
                return@flow
            }

            val rocket = response.docs.first().toDomainModel()

            // Update cache
            rocketCache[rocket.id] = rocket

            emit(Result.success(rocket))
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