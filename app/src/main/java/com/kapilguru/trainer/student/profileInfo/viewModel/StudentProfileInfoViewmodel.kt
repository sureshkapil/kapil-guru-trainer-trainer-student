package com.kapilguru.trainer.student.profileInfo.viewModel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.isPinCodeValid
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.profile.data.SaveStudentProfileResponse
import com.kapilguru.trainer.student.profile.data.StudentProfileData
import com.kapilguru.trainer.student.profile.data.StudentProfileResponse
import com.kapilguru.trainer.student.profileInfo.StudentProfileInfoRepository
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse
import com.kapilguru.trainer.ui.profile.profileInfo.models.CityResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.CountryResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.StateResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentProfileInfoViewmodel(private val studentProfileInfoRepository: StudentProfileInfoRepository) : ViewModel() {
    val uploadImageCourseResponse: MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()
    private val TAG = "ProfileInfoViewmodel"
    val countryList: MutableLiveData<ApiResource<CountryResponce>> = MutableLiveData()
    val stateList: MutableLiveData<ApiResource<StateResponce>> = MutableLiveData()
    val cityList: MutableLiveData<ApiResource<CityResponce>> = MutableLiveData()
    val saveStudentProfileResponse: MutableLiveData<ApiResource<SaveStudentProfileResponse>> = MutableLiveData()
    val profileDataResponse: MutableLiveData<ApiResource<StudentProfileResponse>> = MutableLiveData()
    var profileMutLiveData: MutableLiveData<StudentProfileData> = MutableLiveData()
    var errorDescription: MutableLiveData<String> = MutableLiveData()

    fun getCountryList() {
        countryList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                countryList.postValue(ApiResource.success(studentProfileInfoRepository.getCountryList()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message, code = exception.code)
                )
            } catch (exception: HttpException) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getStateList(countryId: Int) {
        stateList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                stateList.postValue(ApiResource.success(studentProfileInfoRepository.getSateList(countryId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                stateList.postValue(
                    ApiResource.error(data = null, message = exception.message, code = exception.code)
                )
            } catch (exception: HttpException) {
                stateList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                stateList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getCityList(stateId: Int) {
        cityList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cityList.postValue(ApiResource.success(studentProfileInfoRepository.getCityList(stateId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message, code = exception.code)
                )
            } catch (exception: HttpException) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun uploadProfileImage(uploadImageCourse: UploadImageCourse) {
        uploadImageCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                Log.d(TAG, "updatedText: ${uploadImageCourse.baseImage} other_ino_id${uploadImageCourse.sourceId}")
                uploadImageCourseResponse.postValue(
                    ApiResource.success(
                        studentProfileInfoRepository.uploadCourseImage(uploadImageCourse)
                    )
                )
            } catch (e: HttpException) {
                uploadImageCourseResponse.postValue(
                    ApiResource.error(data = null, message = e.code().toString())
                )
            } catch (e: IOException) {
                uploadImageCourseResponse.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun dataValid(): Boolean {
        val profileData = profileMutLiveData.value
        if (TextUtils.isEmpty(profileData?.name)) {
            errorDescription.postValue("Please enter name")
            return false
        }
        if (TextUtils.isEmpty(profileData?.contactNumber)) {
            errorDescription.postValue("Please enter Contact Number")
            return false
        }
        if (TextUtils.isEmpty(profileData?.email_id)) {
            errorDescription.postValue("Please enter Email")
            return false
        }
        if (TextUtils.isEmpty(profileData?.gender)) {
            errorDescription.postValue("Please select Gender")
            return false
        }
        /* if(TextUtils.isEmpty(profileData?.currency)){
             errorDescription.postValue("Please select Currency")
             return false
         }*/
        if (TextUtils.isEmpty(profileData?.addressLine1)) {
            errorDescription.postValue("Please enter Address")
            return false
        }
        if (TextUtils.isEmpty(profileData?.countryId)) {
            errorDescription.postValue("Please select Country")
            return false
        }

        if (TextUtils.isEmpty(profileData?.stateId)) {
            errorDescription.postValue("Please select State")
            return false
        }

        if (TextUtils.isEmpty(profileData?.cityId)) {
            errorDescription.postValue("Please select State")
            return false
        }
        if(!profileData?.postalCode?.isPinCodeValid()!!){
            errorDescription.postValue("Please enter valid Postal Code")
            return false
        }
        return true
    }

    fun dataOrganizationValid(): Boolean {
        val profileData = profileMutLiveData.value
        if (TextUtils.isEmpty(profileData?.orgContactNumber)) {
            errorDescription.postValue("Please enter Contact Number")
            return false
        }
        if (TextUtils.isEmpty(profileData?.officialEmail)) {
            errorDescription.postValue("Please enter Official Email")
            return false
        }
        if (TextUtils.isEmpty(profileData?.orgAddressLine1)) {
            errorDescription.postValue("Please enter Address")
            return false
        }
        if (TextUtils.isEmpty(profileData?.orgCountryId)) {
            errorDescription.postValue("Please select Country")
            return false
        }
        if (TextUtils.isEmpty(profileData?.orgStateId)) {
            errorDescription.postValue("Please select State")
            return false
        }
        if (TextUtils.isEmpty(profileData?.orgCityId)) {
            errorDescription.postValue("Please select City")
            return false
        }
        return true
    }

    fun getProfileData(userId: String) {
        profileDataResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileDataResponse.postValue(ApiResource.success(studentProfileInfoRepository.getStudentProfileData(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message, code = exception.code))
            } catch (exception: HttpException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun updateProfileData() {
        saveStudentProfileResponse.value = ApiResource.loading(null)
//        Log.d(TAG, "updateProfileData profile data: "+profileMutLiveData.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveStudentProfileResponse.postValue(
                    ApiResource.success(
                        studentProfileInfoRepository.updateStudentProfile(
                            profileMutLiveData.value?.userId.toString(), profileMutLiveData.value!!
                        )
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                saveStudentProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message, code = exception.code)
                )
            } catch (exception: HttpException) {
                saveStudentProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                saveStudentProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}