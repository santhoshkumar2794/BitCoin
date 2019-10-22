package com.zestworks.bitcoin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.zestworks.bitcoin.data.BitValue
import com.zestworks.bitcoin.domain.MarketPriceUseCase
import com.zestworks.bitcoin.ui.model.*
import com.zestworks.utils.CoroutineDispatcherProvider
import com.zestworks.utils.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BitCoinViewModelTest {

    private val useCase = Mockito.mock(MarketPriceUseCase::class.java)
    private lateinit var dispatcherProvider: CoroutineDispatcherProvider

    private lateinit var viewModel: BitCoinViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        val dispatcher = TestCoroutineDispatcher()
        dispatcherProvider = CoroutineDispatcherProvider(
            main = dispatcher,
            io = dispatcher,
            computation = dispatcher
        )

        runBlocking {
            Mockito.`when`(useCase.getMarketPrice(any())).thenReturn(
                NetworkResult.Success(
                    emptyList()
                )
            )
        }

        viewModel = BitCoinViewModel(useCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(useCase)
    }

    @Test
    fun `fetching market price for default duration(OneWeek) should return success`() {
        runBlocking {
            assert(viewModel.duration.value!! is OneWeek)
            assert(viewModel.viewState.value!! is Content)

            Mockito.verify(useCase).getMarketPrice(any())
        }
    }

    @Test
    fun `fetching market price for default duration(OneWeek) should return error`() {
        runBlocking {
            Mockito.`when`(useCase.getMarketPrice(any())).thenReturn(
                NetworkResult.Error(
                    "Something went wrong"
                )
            )
            viewModel = BitCoinViewModel(useCase, dispatcherProvider)


            assert(viewModel.duration.value!! is OneWeek)
            assert(viewModel.viewState.value!! is Error)

            Mockito.verify(useCase, times(2)).getMarketPrice(any())
        }
    }

    @Test
    fun `changing the duration to one month should fetch again and return success `() {
        runBlocking {
            val bitValues = listOf(BitValue(255, 789.567))
            Mockito.`when`(useCase.getMarketPrice(any())).thenReturn(
                NetworkResult.Success(bitValues)
            )

            viewModel.onDurationChecked(R.id.oneMonth, true)

            assert(viewModel.duration.value!! is OneMonth)
            assert(viewModel.viewState.value!! is Content)
            assert((viewModel.viewState.value as Content).priceList == bitValues)

            Mockito.verify(useCase, times(2)).getMarketPrice(any())
        }
    }

    @Test
    fun `clicking retry should fetch again`() {
        runBlocking {
            Mockito.`when`(useCase.getMarketPrice(any())).thenReturn(
                NetworkResult.Error(
                    "Something went wrong"
                )
            )
            viewModel = BitCoinViewModel(useCase, dispatcherProvider)


            assert(viewModel.duration.value!! is OneWeek)
            assert(viewModel.viewState.value!! is Error)

            Mockito.`when`(useCase.getMarketPrice(any())).thenReturn(
                NetworkResult.Success(
                    emptyList()
                )
            )

            viewModel.retry()

            Mockito.verify(useCase, times(3)).getMarketPrice(any())
        }
    }
}