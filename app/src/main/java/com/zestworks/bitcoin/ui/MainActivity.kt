package com.zestworks.bitcoin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zestworks.bitcoin.R
import com.zestworks.bitcoin.di.BitCoinInjector


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BitCoinInjector.inject(this)
    }

}
