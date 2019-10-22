package com.zestworks.bitcoin.data

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BitCoinApi {

    @GET("/charts/market-price?format=json")
    suspend fun getMarketPrice(@Query("timespan") timeSpan: String): Response<MarketPriceResponse>

    companion object {
        private const val BASE_URL = "https://api.blockchain.info/"

        fun create(okHttpClient: OkHttpClient): BitCoinApi {
            val gson = GsonBuilder().setLenient().create()
            return Retrofit.Builder()
                .baseUrl(HttpUrl.parse(BASE_URL)!!)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(BitCoinApi::class.java)
        }

        val okHttpClient: OkHttpClient by lazy {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.e("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
        }
    }
}