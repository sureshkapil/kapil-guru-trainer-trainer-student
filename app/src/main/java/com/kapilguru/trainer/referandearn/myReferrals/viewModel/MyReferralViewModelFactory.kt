package com.kapilguru.trainer.referandearn.myReferrals.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.referandearn.myReferrals.MyReferralRepository

class MyReferralViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyReferralViewModel::class.java)) {
            return MyReferralViewModel(MyReferralRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}