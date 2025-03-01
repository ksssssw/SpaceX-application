package com.example.spacexapp.data.repository

import com.example.spacexapp.data.api.SpaceXApi
import com.example.spacexapp.data.models.CompanyInfoResponse
import com.example.spacexapp.domain.models.CompanyInfo
import com.example.spacexapp.domain.repository.CompanyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CompanyRepositoryImpl @Inject constructor(
    private val api: SpaceXApi
) :  CompanyRepository {

    override fun getCompanyInfo(): Flow<Result<CompanyInfo>> = flow {
        try {
            val response = api.getCompanyInfo()
            emit(Result.success(response.toDomainModel()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private fun CompanyInfoResponse.toDomainModel(): CompanyInfo {
        return CompanyInfo(
            name = name,
            founder = founder,
            founded = founded,
            employees = employees,
            vehicles = vehicles,
            launchSites = launchSites,
            testSites = testSites,
            ceo = ceo,
            cto = cto,
            coo = coo,
            ctoPropulsion = ctoPropulsion,
            valuation = valuation,
            headquarters = CompanyInfo.Headquarters(
                address = headquarters.address,
                city = headquarters.city,
                state = headquarters.state
            ),
            links = CompanyInfo.Links(
                website = links.website,
                flickr = links.flickr,
                twitter = links.twitter,
                elonTwitter = links.elonTwitter
            ),
            summary = summary
        )
    }

}