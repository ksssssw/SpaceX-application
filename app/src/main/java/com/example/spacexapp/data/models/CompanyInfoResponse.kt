package com.example.spacexapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyInfoResponse(
    @SerialName("name")
    val name: String,
    @SerialName("founder")
    val founder: String,
    @SerialName("founded")
    val founded: Int,
    @SerialName("employees")
    val employees: Int,
    @SerialName("vehicles")
    val vehicles: Int,
    @SerialName("launch_sites")
    val launchSites: Int,
    @SerialName("test_sites")
    val testSites: Int,
    @SerialName("ceo")
    val ceo: String,
    @SerialName("cto")
    val cto: String,
    @SerialName("coo")
    val coo: String,
    @SerialName("cto_propulsion")
    val ctoPropulsion: String,
    @SerialName("valuation")
    val valuation: Long,
    @SerialName("headquarters")
    val headquarters: Headquarters,
    @SerialName("links")
    val links: Links,
    @SerialName("summary")
    val summary: String
)

@Serializable
data class Headquarters(
    @SerialName("address")
    val address: String,
    @SerialName("city")
    val city: String,
    @SerialName("state")
    val state: String
)

@Serializable
data class Links(
    @SerialName("website")
    val website: String,
    @SerialName("flickr")
    val flickr: String,
    @SerialName("twitter")
    val twitter: String,
    @SerialName("elon_twitter")
    val elonTwitter: String
)