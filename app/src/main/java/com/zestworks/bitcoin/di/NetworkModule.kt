package com.zestworks.bitcoin.di

import com.zestworks.bitcoin.data.BitCoinApi
import com.zestworks.bitcoin.data.BitCoinApi.Companion.okHttpClient
import com.zestworks.utils.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideBitCoinApi(okHttpClient: OkHttpClient) =
        BitCoinApi.create(okHttpClient = okHttpClient)

    @Provides
    @Singleton
    fun provideCoroutineDispatcher() =
        CoroutineDispatcherProvider(
            main = Dispatchers.Main,
            io = Dispatchers.IO,
            computation = Dispatchers.Default
        )
}