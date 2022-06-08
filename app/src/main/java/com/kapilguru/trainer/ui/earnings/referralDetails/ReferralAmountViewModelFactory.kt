package com.kapilguru.trainer.ui.earnings.referralDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class ReferralAmountViewModelFactory (private val apiHelper: ApiHelper,
                                      private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReferralAmountViewModel::class.java)) {
            return ReferralAmountViewModel(
                ReferralAmountRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}