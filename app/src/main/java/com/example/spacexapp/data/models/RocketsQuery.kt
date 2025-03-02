package com.example.spacexapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RocketQueryRequest(
    val query: Map<String, String> = emptyMap(),
    val options: QueryOptions
)

@Serializable
data class QueryOptions(
    val page: Int,
    val limit: Int,
    val sort: Map<String, Int> = mapOf("name" to 1),
    val select: Map<String, Int>? = null
)

@Serializable
data class RocketQueryResponse(
    val docs: List<RocketsResponse>,
    val totalDocs: Int,
    val limit: Int,
    val totalPages: Int,
    val page: Int,
    val pagingCounter: Int,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean,
    val prevPage: Int? = null,
    val nextPage: Int? = null
)