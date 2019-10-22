package com.zestworks.bitcoin.data

import com.zestworks.utils.NetworkResult
import javax.inject.Inject

interface BitRepository {
    suspend fun getMarketPrice(timeSpan: String): NetworkResult<MarketPriceResponse>
}


class BitRepositoryImpl @Inject constructor(private val bitCoinApi: BitCoinApi) : BitRepository {

    override suspend fun getMarketPrice(timeSpan: String): NetworkResult<MarketPriceResponse> {

        try {
            val response = bitCoinApi.getMarketPrice(timeSpan = timeSpan)
            if (response.isSuccessful) {
                return NetworkResult.Success(response.body()!!)
            }
            return NetworkResult.Error("OOPS!! Something went wrong.")
        } catch (exception: Exception) {
            exception.printStackTrace()
            return NetworkResult.Error("OOPS!! Something went wrong.")
        }
    }
}

class BitRepoitory2 : BitRepository{
    override suspend fun getMarketPrice(timeSpan: String): NetworkResult<MarketPriceResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}