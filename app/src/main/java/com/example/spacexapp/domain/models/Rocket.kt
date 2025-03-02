package com.example.spacexapp.domain.models

data class Rocket(
    val id: String,
    val name: String,
    val description: String,
    val firstFlight: String,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    val costPerLaunch: Long,
    val successRatePct: Int,
    val height: Double,
    val diameter: Double,
    val mass: Int,
    val images: List<String>
)