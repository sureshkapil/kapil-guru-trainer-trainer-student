package com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.viewModel

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.MyPosRenewConfirmRepository
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest

class MyPosRenewConfirmViewModel(private val repository :MyPosRenewConfirmRepository) : ViewModel() {
    private val TAG = "MyPosRenewConfirmViewModel"
    var positionSubscriptionData = MyPositionData()
    var initiateTransactionRequest = InitiateTransactionRequest()
}