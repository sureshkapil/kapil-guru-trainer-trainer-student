package com.kapilguru.trainer.enquiries.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.enquiries.EnquiriesRepository
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResData
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResponse
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiryStatusUpdateRequest
import com.kapilguru.trainer.enquiries.todaysFollowUp.model.TodaysFollowUpResData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class EnquiriesViewModel(private val repository: EnquiriesRepository, var context: Application) : AndroidViewModel(context) {
    private val TAG = "EnquiriesViewModel"
    val enquiriesListApiRes: MutableLiveData<ApiResource<EnquiriesResponse>> = MutableLiveData()
    val enquiriesList: MutableLiveData<ArrayList<EnquiriesResData>> = MutableLiveData()
    var kapilGuruEnquiries: MutableLiveData<ArrayList<EnquiriesResData>> = MutableLiveData()
    var offlineEnquiries: MutableLiveData<ArrayList<EnquiriesResData>> = MutableLiveData()
    val showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData()
    val isEnquiryStatusUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val todaysFollowUpListMutLiveData: MutableLiveData<ArrayList<TodaysFollowUpResData>> = MutableLiveData()

    fun getEnquiriesList(trainerId: String) {
        enquiriesListApiRes.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                enquiriesListApiRes.postValue(ApiResource.success(data = repository.getEnquiries(trainerId)))
            } catch (exception: IOException) {
                enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    }

    fun updateEnquiryStatus(enquiryStatusUpdateRequest: EnquiryStatusUpdateRequest) {
        showLoadingIndicator.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.enquiryStatusUpdate(enquiryStatusUpdateRequest)
                if (response.data.insertId != 0) {
                    isEnquiryStatusUpdated.postValue(true)
                } else {
                    isEnquiryStatusUpdated.postValue(false)
                }
            } catch (exception: IOException) {
                enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
            showLoadingIndicator.postValue(false)
        }
    }

    fun getTodayFollowUpList() {
        val trainerId = StorePreferences(context).userId.toString()
        showLoadingIndicator.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todaysFollowUpResponse = repository.getTodaysFollowUp(trainerId)
                todaysFollowUpResponse.todaysFollowUpList?.let { todaysFollowUpListNotNul ->
                    todaysFollowUpListMutLiveData.postValue(todaysFollowUpListNotNul)
                }
            } catch (exception: IOException) {
                enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
        showLoadingIndicator.postValue(false)
    }


}