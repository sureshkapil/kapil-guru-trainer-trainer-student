package com.kapilguru.trainer.ui.earnings.amountDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class AmountViewModelFactory (private val apiHelper: ApiHelper,
                              private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmountViewModel::class.java)) {
            return AmountViewModel(
                AmountDetailsRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}