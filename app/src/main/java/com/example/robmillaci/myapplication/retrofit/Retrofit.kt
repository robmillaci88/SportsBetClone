package com.example.robmillaci.myapplication.retrofit

import com.example.robmillaci.myapplication.pojos.IEventObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Retrofit {
    object HttpClient {
        fun createHttpClient(): OkHttpClient {
            return OkHttpClient().newBuilder()
                .connectTimeout(5L, TimeUnit.SECONDS)
                .readTimeout(5L, TimeUnit.SECONDS)
                .build()
        }
    }

    object Init {
        val retrofitInstance: Retrofit by lazy {

            return@lazy Retrofit.Builder().baseUrl(BASE_URL)
                .client(HttpClient.createHttpClient())
                .addConverterFactory(GsonConverterFactory.create(CustomGson.init()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }

    object CustomGson {
        fun init(): Gson {
            return GsonBuilder()
                .registerTypeAdapter(
                    IEventObject::class.java,
                    InterfaceAdapter<IEventObject>()
                )
                .create()
        }
    }
}