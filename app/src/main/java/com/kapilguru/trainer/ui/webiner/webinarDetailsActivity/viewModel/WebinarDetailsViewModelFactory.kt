package com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.webiner.WebinarRepository
import com.kapilguru.trainer.ui.webiner.addWebinar.AddWebinarRepository
import com.kapilguru.trainer.ui.webiner.addWebinar.viewModel.AddWebinarViewModel

class WebinarDetailsViewModelFactory( private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebinarDetailsViewModel::class.java)) {
            return WebinarDetailsViewModel(
                WebinarRepository(apiHelper),
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}