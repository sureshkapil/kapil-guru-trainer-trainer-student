package com.kapilguru.trainer.ui.earnings.history.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.earnings.history.EarningsHistoryRepository

class EarningsHistoryViewModelFactory(private val apiHelper: ApiHelper,
                                      private val appliaction: Application
): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EarningsHistoryViewModel::class.java)) {
            return EarningsHistoryViewModel(
                EarningsHistoryRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}