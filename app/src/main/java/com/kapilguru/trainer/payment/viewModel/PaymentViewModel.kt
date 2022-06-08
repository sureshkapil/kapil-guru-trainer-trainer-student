package com.kapilguru.trainer.payment.viewModel

import android.app.Application
import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.payment.PaymentRepository
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.InitiateTransactionResponse
import com.kapilguru.trainer.payment.model.TransactionStatusResponse
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PaymentViewModel(private val repository: PaymentRepository,val context: Application) : AndroidViewModel(context) {
    val initiateTransactionResponse : MutableLiveData<ApiResource<InitiateTransactionResponse>> = MutableLiveData()
    val transactionStatusResponse : MutableLiveData<ApiResource<TransactionStatusResponse>> = MutableLiveData()
    lateinit var mInitiateTransactionRequest : InitiateTransactionRequest

    fun callInitiateTransactionApi(initiateTransactionRequest: InitiateTransactionRequest) {
        mInitiateTransactionRequest = initiateTransactionRequest
        initiateTransactionResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                initiateTransactionResponse.postValue(ApiResource.success(data = repository.callInitiationTransactionRequest(initiateTransactionRequest)))
            } catch (exception: HttpException) {
                initiateTransactionResponse.postValue(ApiResource.error(data = null,exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                initiateTransactionResponse.postValue(ApiResource.error(data = null,exception.message ?: "Error Occurred !"))
            }
        }
    }

    fun callTransactionStatusApi(transactionStatusRequest : InitiateTransactionRequest) {
        transactionStatusResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionStatusResponse.postValue(ApiResource.success(data = repository.callTransactionStatusRequest(transactionStatusRequest)))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                transactionStatusResponse.postValue(ApiResource.error(data = null,exception.message() ?: "Error Occured !"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                transactionStatusResponse.postValue(ApiResource.error(data = null,exception.message ?: "Error Occured !"))
            }
        }
    }
}