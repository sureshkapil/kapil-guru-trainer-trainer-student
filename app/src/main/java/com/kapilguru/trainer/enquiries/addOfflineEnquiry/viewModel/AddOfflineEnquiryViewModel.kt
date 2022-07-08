package com.kapilguru.trainer.enquiries.addOfflineEnquiry.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.emailValidation
import com.kapilguru.trainer.enquiries.EnquiriesRepository
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.data.AddEnquiryReq
import com.kapilguru.trainer.isStringEmpty
import com.kapilguru.trainer.isValidMobileNo
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddOfflineEnquiryViewModel(val repository: EnquiriesRepository, var context: Application) : AndroidViewModel(context) {
    val showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData()
    val courseListMutLiveData: MutableLiveData<ArrayList<CourseResponse>> = MutableLiveData()
    val addOfflineEnquiryMutLiveData: MutableLiveData<AddEnquiryReq> = MutableLiveData(AddEnquiryReq())
    val informUser: MutableLiveData<String> = MutableLiveData()
    private var courseList = ArrayList<CourseResponse>()


    fun getCourseList() {
        val trainerId = StorePreferences(context).userId.toString()
        if (courseList.isNotEmpty()) {
            courseListMutLiveData.postValue(courseList)
        } else {
            showLoadingIndicator.postValue(true)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    courseListMutLiveData.postValue(repository.getCoursesList(trainerId).courseResponse as ArrayList<CourseResponse>?)
                } catch (exception: Exception) {

                }
                showLoadingIndicator.postValue(false)
            }
        }
    }

    fun checkAndAddEnquiry() {
        if (isAddEnquiryDataValid()) {
            addEnquiry()
        }
    }

    private fun isAddEnquiryDataValid(): Boolean {
        val addOfflineEnquiry = addOfflineEnquiryMutLiveData.value
        if (addOfflineEnquiry?.fullName.isStringEmpty()) {
            showAppToast(context, "Please enter Name")
            return false
        }
        if (addOfflineEnquiry?.contactNumber.isStringEmpty()) {
            showAppToast(context, "Please enter contact No")
            return false
        }
        if (addOfflineEnquiry?.contactNumber?.isValidMobileNo() == false) {
            showAppToast(context, "Please enter correct contact No")
            return false
        }
        if (!addOfflineEnquiry?.emailId.isStringEmpty() && addOfflineEnquiry?.emailId?.emailValidation() == true) {
            showAppToast(context, "please enter correct mail id")
            return false
        }
        return true
    }

    private fun addEnquiry() {
        showLoadingIndicator.value = true
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addEnquiry(addOfflineEnquiryMutLiveData.value!!)
                informUser.postValue("Enquiry Added Successfully")
            }
        } catch (exception: Exception) {
            informUser.postValue("Add Enquiry Failed")
        }
        showLoadingIndicator.value = false
    }

}