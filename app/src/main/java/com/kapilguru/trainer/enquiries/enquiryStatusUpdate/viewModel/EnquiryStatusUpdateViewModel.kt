package com.kapilguru.trainer.enquiries.enquiryStatusUpdate.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.enquiries.EnquiriesRepository
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.model.EnquiryUpdatedStatusListResData
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiryStatusUpdateRequest
import com.kapilguru.trainer.getDateInApiFormat
import com.kapilguru.trainer.isStringEmpty
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class EnquiryStatusUpdateViewModel(val repository: EnquiriesRepository) : ViewModel() {
    private val TAG = "EnquiryStatusUpdateViewModel"

    //EnquiryId for which we are getting updated status
    var enquiryId: String = ""
    val updatedStatusEnquiryList: MutableLiveData<ArrayList<EnquiryUpdatedStatusListResData>> = MutableLiveData()
    val showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData()
    val informUser: MutableLiveData<String> = MutableLiveData()
    val updateEnquiryStatusRequest: MutableLiveData<EnquiryStatusUpdateRequest> = MutableLiveData(EnquiryStatusUpdateRequest())

    fun getUpdatedEnquiryStatusList() {
        showLoadingIndicator.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val enquiryUpdatedStatusListResponse = repository.getEnquiryUpdatedStatusList(enquiryId)
                enquiryUpdatedStatusListResponse.enquiryUpdatedStatusList?.let { updatedStatusEnquiryListNotNull ->
                    updatedStatusEnquiryList.postValue(updatedStatusEnquiryListNotNull)
                }
            } catch (exception: IOException) {
                informUser.postValue(exception.message ?: "Error Occurred!")
            } catch (exception: HttpException) {
                informUser.postValue(exception.message ?: "Error Occurred!")
            }
            showLoadingIndicator.postValue(false)
        }
    }

    fun checkAndUpdateEnquiry() {
        if (isUpdateEnquiryValid()) {
            updateEnquiryStatus()
        }
    }

    private fun isUpdateEnquiryValid(): Boolean {
        val updateEnquiryReq = updateEnquiryStatusRequest.value
        updateEnquiryReq?.let { updateEnquiryReqNotNull ->
            if (updateEnquiryReqNotNull.status == null) {
                informUser.value = "Please select status"
                return false
            }
            if (updateEnquiryReqNotNull.selectedDate.isStringEmpty()) {
                informUser.value = "Please select date"
                return false
            }
            if (updateEnquiryReqNotNull.selectedTime.isStringEmpty()) {
                informUser.value = "Please select time"
                return false
            }
            return true
        } ?: kotlin.run {
            return false
        }
    }

    private fun updateEnquiryStatus() {
        showLoadingIndicator.value = true
        updateEnquiryStatusRequest.value?.followUpDate = getDateInApiFormat(updateEnquiryStatusRequest.value?.selectedDate!!,updateEnquiryStatusRequest.value?.selectedTime!!)
        Log.d(TAG, "updateEnquiryStatus: request : "+updateEnquiryStatusRequest.value)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateEnquiryStatusRequest.value?.let { updateEnquiryStatusNotNull ->
                   /* val updateEnquiryStatusResponse = repository.enquiryStatusUpdate(updateEnquiryStatusNotNull)
                    if (updateEnquiryStatusResponse.data.insertId != 0) {
                        informUser.postValue("Enquiry updated successfully")
                    }*/
                }
            } catch (exception: IOException) {
                informUser.postValue(exception.message ?: "Error Occurred!")
            } catch (exception: HttpException) {
                informUser.postValue(exception.message ?: "Error Occurred!")
            }
            showLoadingIndicator.postValue(false)
        }
    }
}