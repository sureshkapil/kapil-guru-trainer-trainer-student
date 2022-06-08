package com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.viewModel

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.MyBadgeRenewConfirmRepository
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest

class MyBadgeRenewConfirmViewModel(private val repository : MyBadgeRenewConfirmRepository) : ViewModel() {
    private val TAG = "MyBadgeRenewConfirmViewModel"
    var myBestTrainerData = MyBestTrainerData()
    var initiateTransactionRequest = InitiateTransactionRequest()
}