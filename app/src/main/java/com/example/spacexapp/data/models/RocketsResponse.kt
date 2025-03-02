package com.example.spacexapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketsResponse(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("first_flight")
    val firstFlight: String,
    @SerialName("active")
    val active: Boolean,
    @SerialName("stages")
    val stages: Int,
    @SerialName("boosters")
    val boosters: Int,
    @SerialName("cost_per_launch")
    val costPerLaunch: Long,
    @SerialName("success_rate_pct")
    val successRatePct: Int,
    @SerialName("height")
    val height: Dimension,
    @SerialName("diameter")
    val diameter: Dimension,
    @SerialName("mass")
    val mass: Mass,
    @SerialName("flickr_images")
    val flickrImages: List<String> = emptyList()
)

@Serializable
data class Dimension(
    @SerialName("meters")
    val meters: Double,
    @SerialName("feet")
    val feet: Double
)

@Serializable
data class Mass(
    @SerialName("kg")
    val kg: Int,
    @SerialName("lb")
    val lb: Int
)