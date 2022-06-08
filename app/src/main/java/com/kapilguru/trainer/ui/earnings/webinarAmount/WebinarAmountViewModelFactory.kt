package com.kapilguru.trainer.ui.earnings.webinarAmount

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class WebinarAmountViewModelFactory (private val apiHelper: ApiHelper,
                                     private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebinarAmountViewModel::class.java)) {
            return WebinarAmountViewModel(
                WebinarAmountRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}