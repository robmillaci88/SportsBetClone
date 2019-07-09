package com.example.robmillaci.myapplication.retrofit

import com.example.robmillaci.myapplication.adapters.SectionDataModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

const val BASE_URL = "http://www.mocky.io/v2/"
//Mock JSON HTTP responses
//https://www.mocky.io/

interface BetSportsAPI {
    @GET("5d23db322e000071a2c3f218") //The mocky.io path to retrieve our fake sports data
    fun getSportsData(): Single<ArrayList<SectionDataModel>>

    @GET("5d23db5e2e00003ca7c3f219") //The mocky.io path to retrieve our fake racing data
    fun getRacingData(): Single<ArrayList<SectionDataModel>>
}

