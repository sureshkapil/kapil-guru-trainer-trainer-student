package com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.mySubscriptions.MySubscriptionRepository

class MySubscriptionViewModelFactory(private val apiHelper: ApiHelper, private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MySubscriptionViewModel::class.java)) {
            return MySubscriptionViewModel(MySubscriptionRepository(apiHelper),application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}