package com.zzunee.libraryexample

import android.app.Application
import com.zzunee.libraryexample.model.DataContainer

class LibraryApplication: Application() {
    lateinit var dataContainer: DataContainer

    override fun onCreate() {
        super.onCreate()
        dataContainer = DataContainer(this)
    }
}