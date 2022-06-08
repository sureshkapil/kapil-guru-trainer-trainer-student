package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.BestTrainerCourseRepository
import com.kapilguru.trainer.allSubscription.positionSubscription.PositionSubscriptionRepository

class BestTrainerViewModelFactory(private val apiHelper : ApiHelper,private val application : Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BestTrainerViewModel::class.java)) {
            return BestTrainerViewModel(BestTrainerCourseRepository(apiHelper),application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}