package com.kapilguru.trainer.ui.earnings.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.earnings.model.EarningsRepository

class EarningsDetailsViewModelFactory (private val apiHelper: ApiHelper,
                                       private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EarningsDetailsViewModel::class.java)) {
            return EarningsDetailsViewModel(
                EarningsDetailsRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}