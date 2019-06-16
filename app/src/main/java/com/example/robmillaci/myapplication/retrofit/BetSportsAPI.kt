package com.example.robmillaci.myapplication.retrofit

import com.example.robmillaci.myapplication.adapters.SectionDataModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

const val BASE_URL = "http://www.mocky.io/v2/"
//Mock JSON HTTP responses

interface BetSportsAPI {
    @GET("5d01ea5e3100005e00ab2a7d")
    fun getSportsData(): Single<ArrayList<SectionDataModel>>

    @GET("5d04dd6d320000b63fd74a39")
    fun getRacingData(): Single<ArrayList<SectionDataModel>>
}

