package com.example.robmillaci.myapplication.retrofit

import com.example.robmillaci.myapplication.pojos.IEventObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Creates and returns our singleton retrofit instance.
 * Also creates OkHtppClient and a custom GSON object used as the converter factory for Retrofit - using our custom type adapter
 * Our retrofit instance uses RXJava call adapter so we can return observables/singles from the retrofit call data
 */
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
                .client(HttpClient.createHttpClient()) //sets our implementation of OkHttpClient
                .addConverterFactory(GsonConverterFactory.create(CustomGson.init())) //adds our custom GSON as the converter factory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //uses RXJava as the callback adapter so we can return observables/singles
                .build()
        }
    }

    object CustomGson {
        fun init(): Gson {
            return GsonBuilder()
                .registerTypeAdapter( //Register our custom Interface adapter to successfully deserialize IEventObjects into Sports/Racing objects
                    IEventObject::class.java,
                    InterfaceAdapter<IEventObject>()
                )
                .create()
        }
    }
}