package com.zestworks.bitcoin.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zestworks.bitcoin.R
import com.zestworks.bitcoin.domain.MarketPriceUseCase
import com.zestworks.utils.CoroutineDispatcherProvider
import com.zestworks.utils.NetworkResult
import com.zestworks.utils.exhaustive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class BitCoinViewModel @Inject constructor(
    private val marketPriceUseCase: MarketPriceUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider
) :
    ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(dispatcherProvider.main + job)

    private val _duration = MutableLiveData<Duration>().apply { value =
        OneWeek
    }
    val duration: LiveData<Duration> = _duration

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getMarketPrice()
    }

    private fun getMarketPrice() {
        scope.launch(dispatcherProvider.io) {
            _viewState.postValue(Loading)
            val marketPrice = marketPriceUseCase.getMarketPrice(_duration.value ?: OneWeek)
            when (marketPrice) {
                is NetworkResult.Success -> _viewState.postValue(
                    Content(
                        marketPrice.data
                    )
                )
                is NetworkResult.Error -> _viewState.postValue(
                    Error(
                        marketPrice.errorMessage
                    )
                )
            }.exhaustive
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun onDurationChecked(checkedId: Int, checked: Boolean) {
        val duration = when (checkedId) {
            R.id.oneMonth -> OneMonth
            R.id.sixMonths -> SixMonths
            R.id.oneYear -> OneYear
            else -> OneWeek
        }
        if (!checked && _duration.value != duration) {
            return
        }
        _duration.value = duration
        getMarketPrice()
    }

    fun retry() {
        getMarketPrice()
    }
}