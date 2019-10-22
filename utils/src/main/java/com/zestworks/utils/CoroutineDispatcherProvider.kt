package com.zestworks.utils

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatcherProvider(
    val main : CoroutineDispatcher,
    val io : CoroutineDispatcher,
    val computation : CoroutineDispatcher
)