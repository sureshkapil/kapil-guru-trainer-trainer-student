package com.kapilguru.trainer.ui.webiner.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.webiner.WebinarRepository

class WebinarViewModelFactory (private val apiHelper: ApiHelper,
                               private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebinarViewModel::class.java)) {
            return WebinarViewModel(
                WebinarRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}