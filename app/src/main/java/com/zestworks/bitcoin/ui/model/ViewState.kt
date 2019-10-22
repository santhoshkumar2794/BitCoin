package com.zestworks.bitcoin.ui.model

import com.zestworks.bitcoin.data.BitValue

sealed class ViewState
object Loading : ViewState()
data class Content(val priceList: List<BitValue>) : ViewState()
data class Error(val errorMessage: String) : ViewState()