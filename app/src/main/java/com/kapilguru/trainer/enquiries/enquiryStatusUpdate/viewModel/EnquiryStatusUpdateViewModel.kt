package com.kapilguru.trainer.enquiries.enquiryStatusUpdate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.enquiries.EnquiriesRepository
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.model.EnquiryUpdatedStatusListResData
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiryStatusUpdateRequest
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
    val updateEnquiryStatusRequest : MutableLiveData<EnquiryStatusUpdateRequest> = MutableLiveData()

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
}