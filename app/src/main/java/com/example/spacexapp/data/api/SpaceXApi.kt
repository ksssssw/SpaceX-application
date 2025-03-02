package com.example.spacexapp.data.api

import com.example.spacexapp.data.models.CompanyInfoResponse
import com.example.spacexapp.data.models.RocketQueryRequest
import com.example.spacexapp.data.models.RocketQueryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SpaceXApi {
    @GET("v4/company")
    suspend fun getCompanyInfo(): CompanyInfoResponse

    @POST("v4/rockets/query")
    suspend fun getRocketsPaginated(@Body queryRequest: RocketQueryRequest): RocketQueryResponse
}