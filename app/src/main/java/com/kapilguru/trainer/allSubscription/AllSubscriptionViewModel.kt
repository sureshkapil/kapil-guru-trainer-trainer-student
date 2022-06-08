package com.kapilguru.trainer.allSubscription

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.models.UpdateKycRequest
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MySubscriptionResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AllSubscriptionViewModel(private val allSubscriptionRepository: AllSubscriptionRepository) : ViewModel() {
    val mySubscriptionResponse: MutableLiveData<ApiResource<MySubscriptionResponse>> = MutableLiveData()
    val allSubscriptionsList: MutableLiveData<ArrayList<AllSubscriptionsData>> = MutableLiveData()
    val packageSubscriptionList = ArrayList<AllSubscriptionsData>()
    var positionSubscriptionList = ArrayList<AllSubscriptionsData>()
    val bestTrainerSubscriptionList = ArrayList<AllSubscriptionsData>()
    var myPackageSubscriptionList: MutableLiveData<ArrayList<MyPackageData>> = MutableLiveData()
    var myPositionSubscriptionList: MutableLiveData<ArrayList<MyPositionData>> = MutableLiveData()
    var myBestTrainerSubscriptionList: MutableLiveData<ArrayList<MyBestTrainerData>> = MutableLiveData()
    lateinit var selectedSubscription: AllSubscriptionsData
    var updateKycApiResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()

    var getAllSubscriptions = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = allSubscriptionRepository.getAllSubscription()))
        } catch (e: HttpException) {
            emit(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
        } catch (e: IOException) {
            emit(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
        }
    }

    fun getMySubscriptionList(trainerId: String) {
        mySubscriptionResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mySubscriptionResponse.postValue(ApiResource.success(allSubscriptionRepository.getMySubscriptions(trainerId)))
            } catch (exception: HttpException) {
                mySubscriptionResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                mySubscriptionResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateKyc(trainerId: String, updateKycRequest: UpdateKycRequest) {
        updateKycApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateKycApiResponse.postValue(ApiResource.success(allSubscriptionRepository.updateKyc(trainerId, updateKycRequest)))
            } catch (exception: HttpException) {
                updateKycApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                updateKycApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}