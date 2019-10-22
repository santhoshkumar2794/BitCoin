package com.zestworks.bitcoin.di

import com.zestworks.bitcoin.data.BitRepository
import com.zestworks.bitcoin.data.BitRepositoryImpl
import com.zestworks.bitcoin.domain.MarketPriceUseCase
import com.zestworks.bitcoin.domain.MarketPriceUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class BitCoinModule {

    @Binds
    abstract fun bindBitRepository(bitRepositoryImpl: BitRepositoryImpl): BitRepository

    @Binds
    abstract fun marketPriceUseCase(marketPriceUseCaseImpl: MarketPriceUseCaseImpl): MarketPriceUseCase
}