package com.example.andrey.kotlinmvvm.network

import com.example.andrey.kotlinmvvm.model.WalletData
import com.example.andrey.kotlinmvvm.utils.BASE_URL
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.Inet4Address
import java.util.*

interface WalletApi{
    @GET("api/addr/{address}")
    fun getWalletData(@Path("address") address: String): Observable<WalletData>

    companion object Factory{
        fun create(): WalletApi{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            logging.level = HttpLoggingInterceptor.Level.BODY

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(logging).build())
                .build()
                .create(WalletApi::class.java)
        }
    }
}