package com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.viewModel

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.PositionSubConfirmRepository
import com.kapilguru.trainer.allSubscription.positionSubscription.model.TrainerCourseData
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest

class PositionSubConfirmViewModel(private val repository: PositionSubConfirmRepository) : ViewModel() {
    private val TAG = "PositionSubConfirmViewModel"
    var allSubscriptionsData: AllSubscriptionsData? = AllSubscriptionsData()
    var positionSubData = TrainerCourseData()
    var isLaunchedForRenewal: Boolean = false
    var initiateTransactionRequest = InitiateTransactionRequest()
}