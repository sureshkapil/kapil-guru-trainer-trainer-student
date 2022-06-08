package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.viewModel

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.confirmation.BadgeSubConfirmRepository
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.BestTrainerBadgeData
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.BestTrainerCourseData
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest

class BadgeSubConfirmViewModel(private val repository : BadgeSubConfirmRepository) : ViewModel()  {
    private val TAG= "BadgeSubConfirmViewModel"
    lateinit var badgeData : AllSubscriptionsData
    lateinit var courseData : BestTrainerCourseData
    var initiateTransactionRequest = InitiateTransactionRequest()
}