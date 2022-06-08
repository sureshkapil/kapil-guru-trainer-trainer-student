package com.kapilguru.trainer.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData


class AppConnectivityCheck(var context: Context) {
    var networkChangeObserver: MutableLiveData<Boolean> = MutableLiveData(true)
    private lateinit var connectivityManager: ConnectivityManager

    init {
        networkCheck()
    }

    private fun networkCheck() {
        connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java)
        } else ({
            context.getSystemService(Context.CONNECTIVITY_SERVICE)
        }) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                    ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
//                    networkChangeObserver.postValue(true)
                }

                override fun onLost(network: Network) {
                    networkChangeObserver.postValue(false)
                }
            })
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }


    fun getConnectivityStatus() : Int{
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val capabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                                result = 2
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                result = 1
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                                result = 3
                    }
                    else -> {
                        result = 4
                    }
                }
            }else{
                result = 4
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                // connected to the internet
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                        result = 2
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                        result = 1
                } else if (activeNetwork.type == ConnectivityManager.TYPE_VPN) {
                        result = 3
                } else {
                    result = 4
                }
            }else{
                result = 4
            }
        }

        return result
    }

}