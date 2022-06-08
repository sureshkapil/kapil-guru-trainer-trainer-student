package com.kapilguru.trainer.payment.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.payment.PaymentRepository

class PaymentViewModelFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(PaymentRepository(apiHelper), application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}