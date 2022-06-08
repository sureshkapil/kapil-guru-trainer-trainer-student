package com.kapilguru.trainer.payment

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest

class PaymentRepository(private val apiHelper: ApiHelper) {
    suspend fun callInitiationTransactionRequest(initiateTransactionRequest: InitiateTransactionRequest) =
        apiHelper.callInitiationTransactionApi(initiateTransactionRequest)

    suspend fun callTransactionStatusRequest(initiateTransactionRequest: InitiateTransactionRequest) =
        apiHelper.callTransactionStatusApi(initiateTransactionRequest)
}