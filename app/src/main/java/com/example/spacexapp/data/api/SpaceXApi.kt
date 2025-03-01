package com.example.spacexapp.data.api

import com.example.spacexapp.data.models.CompanyInfoResponse
import retrofit2.http.GET

interface SpaceXApi {
    @GET("v4/company")
    suspend fun getCompanyInfo(): CompanyInfoResponse
}