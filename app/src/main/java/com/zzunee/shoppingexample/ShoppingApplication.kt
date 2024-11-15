package com.zzunee.shoppingexample

import android.app.Application
import com.zzunee.shoppingexample.model.DataContainer

class ShoppingApplication: Application() {
    lateinit var dataContainer: DataContainer

    override fun onCreate() {
        super.onCreate()
        dataContainer = DataContainer(this)
    }
}