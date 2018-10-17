package com.example.andrey.kotlinmvvm.viewModel

import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.text.TextUtils
import android.util.AndroidException
import android.util.Log
import android.view.View
import com.example.andrey.kotlinmvvm.BuildConfig
import com.example.andrey.kotlinmvvm.MainActivity
import com.example.andrey.kotlinmvvm.model.WalletData
import com.example.andrey.kotlinmvvm.network.WalletApi
import com.example.andrey.kotlinmvvm.utils.BASE_URL
import com.example.andrey.kotlinmvvm.utils.BitcoinWalletValidator
import com.example.andrey.kotlinmvvm.utils.TAG
import com.example.andrey.kotlinmvvm.utils.TEST_ADDRESS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private var walletApi: WalletApi
    private lateinit var compositeDisposable: Disposable
    private val bitcoinWalletValidator: BitcoinWalletValidator
    var balance: Double
    var loading: Boolean

    val _address = ObservableField<String>()
    var address: String?
        get() = _address.get()
        set(value) = _address.set(value)

    init {
        Log.d(TAG, "init was called")
        walletApi = WalletApi.create()
        address = TEST_ADDRESS
        bitcoinWalletValidator = BitcoinWalletValidator()
        balance = 0.0
        loading = false
    }

    fun getWalletData() {
        Log.d(TAG, "recieved Address: ${address}")

        if (!TextUtils.isEmpty(address)) {
            if (bitcoinWalletValidator.validateAddress(address!!)) {
              //  loading = true
                compositeDisposable =
                        walletApi.getWalletData(address!!)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ walletData ->
                                Log.d(TAG, "Balance for ${walletData.addrStr} is ${walletData.balance}")
                                balance = walletData.balance
                          //      loading = false
                            }, { error ->
                                Log.e(TAG, "getting wallet data error")
                                error.printStackTrace()
                          //      loading = false
                            })
            } else
                Log.d(TAG, "wallet address is invalid")
        } else
            Log.d(TAG, "field can not be empty")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.dispose()
    }
}