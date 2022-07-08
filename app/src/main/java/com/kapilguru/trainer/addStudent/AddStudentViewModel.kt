package com.kapilguru.trainer.addStudent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.emailValidation
import com.kapilguru.trainer.generateUuid
import com.kapilguru.trainer.isValidMobileNo
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApi
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseApi
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AddStudentViewModel(
    private val repository: AddStudentRepository, application: Application
) : AndroidViewModel(application) {

    var checkStudentResponse: MutableLiveData<ApiResource<CheckStudentResponse>> = MutableLiveData()
    var checkStudentRequest: MutableLiveData<CheckStudentRequest> = MutableLiveData(CheckStudentRequest())
    var name : MutableLiveData<String> = MutableLiveData("")

    var errorText: MutableLiveData<String> = MutableLiveData("")

    var studentId: MutableLiveData<Int> = MutableLiveData(0)

    // Course Info
    var courseList: MutableLiveData<ApiResource<CourseApi>> = MutableLiveData()
    var courseListApi = arrayListOf<CourseResponse>()
    var courseId :Int =0


    // Batch Info
    var batchList: MutableLiveData<ApiResource<BatchListApi>> = MutableLiveData()
    var batchListApi = arrayListOf<BatchListResponse>()
    var batchId :Int =0


    var onlineStudentRequest: MutableLiveData<OnlineStudentRequest> = MutableLiveData()
    var addOnlineStudentResponse: MutableLiveData<ApiResource<AddOnlineStudentResponse>> = MutableLiveData()

    var trainerId: Int =0
    var tenantId: Int=0

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
        tenantId = pref.tenantId
    }

    fun checkStudent() {
        checkStudentResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkStudentResponse.postValue(ApiResource.success(data = repository.checkStudent(checkStudentRequest.value!!)))
            } catch (exception: HttpException) {
                checkStudentResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                checkStudentResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


    fun getCourseList() {
        viewModelScope.launch(Dispatchers.IO) {
            courseList.postValue(ApiResource.loading(data = null))
            try {
                courseList.postValue(ApiResource.success(data = repository.getCousesList(trainerId.toString())))
            } catch (exception: HttpException) {
                courseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                courseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getBatchList(courseid: String) {
        val batchListRequest = BatchListApiRequest(trainerId, courseid)
        viewModelScope.launch (Dispatchers.IO) {
            batchList.postValue(ApiResource.loading(data = null))
            try {
                batchList.postValue(ApiResource.success(data = repository.getBatchList(batchListRequest)))
            } catch (exception: HttpException) {
                batchList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                batchList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun addOnlineStudent() {
        val onlineStudentRequest: OnlineStudentRequest = getOnlineStudentRequestInfo()
        viewModelScope.launch (Dispatchers.IO) {
            addOnlineStudentResponse.postValue(ApiResource.loading(data = null))
            try {
                addOnlineStudentResponse.postValue(ApiResource.success(data = repository.addOnlineStudent(onlineStudentRequest)))
            } catch (exception: HttpException) {
                addOnlineStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                addOnlineStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun validateUserData(): Boolean {
        var shouldAllow: Boolean = true
        when {
            checkStudentRequest.value?.emailId!!.isEmpty() -> {
                errorText.value = "Please Enter Email Id"
                shouldAllow = false
            }
            checkStudentRequest.value?.emailId!!.emailValidation() -> {
                errorText.value = "Please Enter valid Email Id"
                shouldAllow = false
            }
            checkStudentRequest.value?.contactNumber!!.isEmpty() -> {
                errorText.value = "Please Phone Number"
                shouldAllow = false
            }
            checkStudentRequest.value?.contactNumber!!.isValidMobileNo() -> {
                errorText.value = "Please Enter valid phone Number"
                shouldAllow = false
            }
            courseId == 0 -> {
                errorText.value = "Please Select Course"
                shouldAllow = false
            }
        }
        return shouldAllow
    }

    private fun getOnlineStudentRequestInfo(): OnlineStudentRequest {
        return OnlineStudentRequest(
            emailId = checkStudentRequest.value?.emailId,
            tenantId =tenantId,
            countryCode = "91",
            courseId = courseId.toString(),
            batchId = batchId,
            name = name.value,
            studentId = studentId.value,
            isOtherCountryCode = 0,
            uuid= generateUuid().toString(),
            contactNumber = checkStudentRequest.value?.contactNumber,
            isAddedByTrainer = 1,
            trainerId = trainerId
        )
    }

}