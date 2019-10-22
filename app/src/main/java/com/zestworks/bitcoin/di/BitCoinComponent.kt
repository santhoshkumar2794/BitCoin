package com.zestworks.bitcoin.di

import com.zestworks.bitcoin.ui.MainActivity
import com.zestworks.bitcoin.domain.MarketPriceUseCase
import com.zestworks.bitcoin.ui.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, BitCoinModule::class])
interface BitCoinComponent {

    fun marketPriceUseCase(): MarketPriceUseCase

    fun inject(mainActivity: MainActivity): MainActivity

    fun inject(mainFragment: MainFragment): MainFragment
}