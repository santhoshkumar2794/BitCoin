package com.zestworks.bitcoin.domain

import com.zestworks.bitcoin.data.BitRepository
import com.zestworks.bitcoin.data.BitValue
import com.zestworks.bitcoin.ui.model.*
import com.zestworks.utils.NetworkResult
import com.zestworks.utils.exhaustive
import javax.inject.Inject

interface MarketPriceUseCase {
    suspend fun getMarketPrice(duration: Duration): NetworkResult<List<BitValue>>
}

class MarketPriceUseCaseImpl @Inject constructor(
    private val bitRepository: BitRepository
) :
    MarketPriceUseCase {
    override suspend fun getMarketPrice(duration: Duration): NetworkResult<List<BitValue>> {
        val days = when (duration) {
            OneWeek -> "7days"
            OneMonth -> "30days"
            SixMonths -> "180days"
            OneYear -> "365days"
        }.exhaustive

        return when (val result = bitRepository.getMarketPrice(days)) {
            is NetworkResult.Success -> NetworkResult.Success(result.data.values)
            is NetworkResult.Error -> result
        }.exhaustive
    }
}