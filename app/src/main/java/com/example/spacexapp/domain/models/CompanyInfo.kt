package com.example.spacexapp.domain.models

data class CompanyInfo(
    val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    val vehicles: Int,
    val launchSites: Int,
    val testSites: Int,
    val ceo: String,
    val cto: String,
    val coo: String,
    val ctoPropulsion: String,
    val valuation: Long,
    val headquarters: Headquarters,
    val links: Links,
    val summary: String
) {
    data class Headquarters(
        val address: String,
        val city: String,
        val state: String
    )

    data class Links(
        val website: String,
        val flickr: String,
        val twitter: String,
        val elonTwitter: String
    )
}