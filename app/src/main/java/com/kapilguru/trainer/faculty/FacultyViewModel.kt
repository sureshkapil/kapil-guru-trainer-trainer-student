package com.kapilguru.trainer.faculty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.emailValidation
import com.kapilguru.trainer.isValidMobileNo
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FacultyViewModel(private val repository: FacultyRepository) : ViewModel() {

    var resultDat: MutableLiveData<ApiResource<AllStudentsListPerTrainerApi>> = MutableLiveData()

    var addFacultyRequest: MutableLiveData<AddFacultyRequest> = MutableLiveData(AddFacultyRequest())

    var addFacultyResponse: MutableLiveData<ApiResource<AddFacultyResponse>> = MutableLiveData()

    var getFacultyListResponse: MutableLiveData<ApiResource<FacultyListResponse>> = MutableLiveData()

    var facultySettingsModel: MutableLiveData<ApiResource<FacultySettingsModel>> = MutableLiveData()

    var facultySettingsModelApi: MutableLiveData<FacultySettingsModel> = MutableLiveData()
    var updateFacultyId: String? = null
    var isFromEdit: MutableLiveData<Boolean> =MutableLiveData(false)

    var updateFacultySettingsResponse: MutableLiveData<ApiResource<FacultySettingsResponse>> = MutableLiveData()

    private val TAG = "FacultyViewModel"

    var showText: MutableLiveData<String> = MutableLiveData("")

    fun addFaculty(trainerId: Int, tenentId: Int) {
        addFacultyRequest.value?.trainerId = trainerId
        addFacultyRequest.value?.tenantId = tenentId
        addFacultyResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addFacultyResponse.postValue(ApiResource.success(data = repository.addFaculty(addFacultyRequest.value!!)))
            } catch (exception: HttpException) {
                addFacultyResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                addFacultyResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


    fun validateAddFaculty(): Boolean {
        var isValid: Boolean = true
        when {
            addFacultyRequest.value?.name?.isEmpty()!! -> {
                showText.value = "please Enter Name"
                isValid = false
            }
            addFacultyRequest.value?.emailId?.isEmpty()!! -> {
                showText.value = "please Enter Valid Email"
                isValid = false
            }
            addFacultyRequest.value?.emailId?.emailValidation()!! -> {
                showText.value = "please Enter Valid Email"
                isValid = false
            }
            addFacultyRequest.value?.emailId?.isValidMobileNo()!! -> {
                showText.value = "please Enter Valid Phone Number"
                isValid = false
            }
        }
        return isValid
    }

    fun getFaculty(getFacultyRequest: GetFacultyRequest) {
        getFacultyListResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getFacultyListResponse.postValue(ApiResource.success(data = repository.getFaculty(getFacultyRequest)))
            } catch (exception: HttpException) {
                getFacultyListResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                getFacultyListResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


    fun updateFaculty(id: String,facultySettingsModel: FacultySettingsModel) {
        updateFacultySettingsResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateFacultySettingsResponse.postValue(ApiResource.success(data = repository.updateFaculty(id, facultySettingsModel)))
            } catch (exception: HttpException) {
                updateFacultySettingsResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                updateFacultySettingsResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }

}