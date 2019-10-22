package com.zestworks.bitcoin.di

import com.zestworks.bitcoin.ui.MainActivity
import com.zestworks.bitcoin.ui.MainFragment

object BitCoinInjector {

    private fun bitCoinDaggerComponent() =
        DaggerBitCoinComponent.builder()
            .networkModule(NetworkModule())
            .build()!!

    fun inject(activity: MainActivity) {
        bitCoinDaggerComponent().inject(activity)
    }

    fun inject(mainFragment: MainFragment){
        bitCoinDaggerComponent().inject(mainFragment)
    }
}