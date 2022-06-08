package com.kapilguru.trainer.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class HomeScreenViewModelFactory(private val apiHelper: ApiHelper,
                                 private val appliaction: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(
                HomeScreenRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}