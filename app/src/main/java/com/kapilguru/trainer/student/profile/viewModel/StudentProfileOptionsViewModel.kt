package com.kapilguru.trainer.student.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.profile.StudentProfileOptionsRepository
import com.kapilguru.trainer.student.profile.data.StudentProfileData
import com.kapilguru.trainer.student.profile.data.StudentProfileResponse
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.CheckOTPResponce
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentProfileOptionsViewModel(private val studentProfileOptionsRepository: StudentProfileOptionsRepository) : ViewModel() {
    private val TAG: String = "ProfileOptionsViewModel"
    var profileDataResponse: MutableLiveData<ApiResource<StudentProfileResponse>> = MutableLiveData()
    var getOTPResponce: MutableLiveData<ApiResource<GetOTPResponce>> = MutableLiveData()
    var checkOTPResponce: MutableLiveData<ApiResource<CheckOTPResponce>> = MutableLiveData()
    var profilePercentage: MutableLiveData<Int> = MutableLiveData()
    var studentProfileData: StudentProfileData = StudentProfileData()

    fun getOTP(getOTPRequest: GetOTPRequest) {
        getOTPResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getOTPResponce.postValue(ApiResource.success(studentProfileOptionsRepository.getOTRequest(getOTPRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                getOTPResponce.postValue(ApiResource.error(data = null, message = exception.message, code = exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                getOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                getOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getProfileData(userId: String) {
        profileDataResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileDataResponse.postValue(ApiResource.success(studentProfileOptionsRepository.getStudentProfileData(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message))
            } catch (exception: IOException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: Exception) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun checkOTP(checkOTPRequest: CheckOTPRequest) {
        checkOTPResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkOTPResponce.postValue(ApiResource.success(studentProfileOptionsRepository.checkOTP(checkOTPRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                checkOTPResponce.postValue(ApiResource.error(data = null, message = exception.message, code = exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                checkOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                checkOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}