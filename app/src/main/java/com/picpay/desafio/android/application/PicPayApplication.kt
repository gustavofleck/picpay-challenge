package com.picpay.desafio.android.application

import android.app.Application
import com.picpay.desafio.android.di.picPayModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PicPayApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PicPayApplication)
            modules(picPayModule)
        }
    }

}
