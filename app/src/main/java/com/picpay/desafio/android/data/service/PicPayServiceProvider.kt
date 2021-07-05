package com.picpay.desafio.android.data.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PicPayServiceProvider(
    private val context: Context
) {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val url = BuildConfig.SERVICE_URL

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, (10*1024)))
            .addInterceptor(interceptor)
            .build()
    }

    private val gson: Gson by lazy { GsonBuilder().create() }

    fun service(): PicPayService =
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PicPayService::class.java)

}
