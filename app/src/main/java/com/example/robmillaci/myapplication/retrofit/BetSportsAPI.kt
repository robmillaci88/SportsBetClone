package com.example.robmillaci.myapplication.retrofit

import com.example.robmillaci.myapplication.adapters.SectionDataModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

const val BASE_URL = "http://www.mocky.io/v2/"
//Mock JSON HTTP responses

interface BetSportsAPI {
    @GET("5d0adabe2f00002d00e3ed15")
    fun getSportsData(): Single<ArrayList<SectionDataModel>>

    @GET("5d0add1e2f00000d00e3ed19")
    fun getRacingData(): Single<ArrayList<SectionDataModel>>
}

