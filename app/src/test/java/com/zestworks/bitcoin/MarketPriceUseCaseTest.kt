package com.zestworks.bitcoin

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.zestworks.bitcoin.data.BitRepository
import com.zestworks.bitcoin.data.BitValue
import com.zestworks.bitcoin.data.MarketPriceResponse
import com.zestworks.bitcoin.domain.MarketPriceUseCase
import com.zestworks.bitcoin.domain.MarketPriceUseCaseImpl
import com.zestworks.bitcoin.ui.model.OneWeek
import com.zestworks.utils.NetworkResult
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MarketPriceUseCaseTest {

    private val repository = Mockito.mock(BitRepository::class.java)

    private lateinit var marketPriceUseCase: MarketPriceUseCase

    @Before
    fun setup() {
        marketPriceUseCase = MarketPriceUseCaseImpl(repository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `calling get market price returns a success`() {
        runBlocking {
            Mockito.`when`(repository.getMarketPrice(any()))
                .thenReturn(NetworkResult.Success(MarketPriceResponse.default()))

            val marketPrice = marketPriceUseCase.getMarketPrice(OneWeek)
            assert(marketPrice is NetworkResult.Success)
            assert((marketPrice as NetworkResult.Success<List<BitValue>>).data.isEmpty())

            Mockito.verify(repository).getMarketPrice(any())
        }
    }

    @Test
    fun `calling get market price returns a error`() {
        runBlocking {
            val errorMessage = "Something went wrong"
            Mockito.`when`(repository.getMarketPrice(any()))
                .thenReturn(NetworkResult.Error(errorMessage))

            val marketPrice = marketPriceUseCase.getMarketPrice(OneWeek)
            assert(marketPrice is NetworkResult.Error)
            assert((marketPrice as NetworkResult.Error).errorMessage == errorMessage)

            Mockito.verify(repository).getMarketPrice(any())
        }
    }
}