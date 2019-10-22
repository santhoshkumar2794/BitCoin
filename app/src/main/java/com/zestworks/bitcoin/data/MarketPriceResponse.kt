package com.zestworks.bitcoin.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class MarketPriceResponse(
    val description: String,
    val name: String,
    val period: String,
    val status: String,
    val unit: String,
    val values: List<BitValue>
) {
    companion object {
        fun default(): MarketPriceResponse {
            return MarketPriceResponse(
                description = "",
                name = UUID.randomUUID().toString(),
                period = "",
                status = "",
                unit = "",
                values = emptyList()
            )
        }
    }
}

data class BitValue(
    val x: Int,
    val y: Double
)