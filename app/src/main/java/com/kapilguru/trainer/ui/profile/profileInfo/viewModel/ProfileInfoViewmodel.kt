package com.kapilguru.trainer.ui.profile.profileInfo.viewModel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.isPinCodeValid
import com.kapilguru.trainer.isStringEmpty
import com.kapilguru.trainer.isValidAadharNo
import com.kapilguru.trainer.isValidGST
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse
import com.kapilguru.trainer.ui.profile.data.ProfileData
import com.kapilguru.trainer.ui.profile.data.ProfileResponse
import com.kapilguru.trainer.ui.profile.data.SaveProfileResponse
import com.kapilguru.trainer.ui.profile.profileInfo.ProfileInfoRepository
import com.kapilguru.trainer.ui.profile.profileInfo.models.CityResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.CountryResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.StateResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class ProfileInfoViewmodel(private val profileInfoRepository: ProfileInfoRepository) : ViewModel() {
    val uploadImageCourseResponse: MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()
    private val TAG = "ProfileInfoViewmodel"
    val countryList: MutableLiveData<ApiResource<CountryResponce>> = MutableLiveData()
    val orgCountryList: MutableLiveData<ApiResource<CountryResponce>> = MutableLiveData()
    val stateList: MutableLiveData<ApiResource<StateResponce>> = MutableLiveData()
    val orgStateList: MutableLiveData<ApiResource<StateResponce>> = MutableLiveData()
    val cityList: MutableLiveData<ApiResource<CityResponce>> = MutableLiveData()
    val orgCityList: MutableLiveData<ApiResource<CityResponce>> = MutableLiveData()
    var showOrganisationDetails: MutableLiveData<Boolean> = MutableLiveData()
    var hideOrganisationDetails: MutableLiveData<Boolean> = MutableLiveData()
    val saveProfileResponse: MutableLiveData<ApiResource<SaveProfileResponse>> = MutableLiveData()
    val profileDataResponse: MutableLiveData<ApiResource<ProfileResponse>> = MutableLiveData()
    var profileMutLiveData: MutableLiveData<ProfileData> = MutableLiveData()
    var errorDescription: MutableLiveData<String> = MutableLiveData()
    var imageUpdated: Boolean = false
    var uploadAadharResponse : MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()
    var uploadPanResponse : MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()

    fun getCountryList() {
        countryList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                countryList.postValue(ApiResource.success(profileInfoRepository.getCountryList()))
            } catch (exception: HttpException) {
                countryList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                countryList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getStateList(countryId: Int) {
        stateList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                stateList.postValue(ApiResource.success(profileInfoRepository.getSateList(countryId)))
            } catch (exception: HttpException) {
                stateList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                stateList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getCityList(stateId: Int) {
        cityList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cityList.postValue(ApiResource.success(profileInfoRepository.getCityList(stateId)))
            } catch (exception: HttpException) {
                cityList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                cityList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getOrgCountryList() {
        orgCountryList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                orgCountryList.postValue(ApiResource.success(profileInfoRepository.getCountryList()))
            } catch (exception: HttpException) {
                orgCountryList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                orgCountryList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getOrgStateList(countryId: Int) {
        orgStateList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                orgStateList.postValue(ApiResource.success(profileInfoRepository.getSateList(countryId)))
            } catch (exception: HttpException) {
                orgStateList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                orgStateList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getOrgCityList(stateId: Int) {
        orgCityList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                orgCityList.postValue(ApiResource.success(profileInfoRepository.getCityList(stateId)))
            } catch (exception: HttpException) {
                orgCityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                orgCityList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun uploadProfileImage(uploadImageCourse: UploadImageCourse) {
        uploadImageCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "updatedText: ${uploadImageCourse.baseImage} other_ino_id${uploadImageCourse.sourceId}")
                uploadImageCourseResponse.postValue(ApiResource.success(profileInfoRepository.uploadCourseImage(uploadImageCourse)))
            } catch (e: HttpException) {
                uploadImageCourseResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            } catch (e: IOException) {
                uploadImageCourseResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun dataValid(shouldCheckGST: Boolean): Boolean {
        val profileData = profileMutLiveData.value
        if (TextUtils.isEmpty(profileData?.title)) {
            errorDescription.postValue("Please select Title")
            return false
        }
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
        /*if(TextUtils.isEmpty(profileData?.currency)){
            errorDescription.postValue("Please select Currency")
            return false
        }*/
        if (profileData?.yearsOfExp == null) {
            errorDescription.postValue("Please enter Total Experience")
            return false
        }
        if (profileData.addressLine1.isStringEmpty()) {
            errorDescription.postValue("Please enter Address")
            return false
        }
        if (profileData.addressLine2.isStringEmpty()) {
            errorDescription.postValue("Please enter Area")
            return false
        }
        if (TextUtils.isEmpty(profileData.countryId)) {
            errorDescription.postValue("Please select Country")
            return false
        }

        if (TextUtils.isEmpty(profileData.stateId)) {
            errorDescription.postValue("Please select State")
            return false
        }

        if (TextUtils.isEmpty(profileData.cityId)) {
            errorDescription.postValue("Please select State")
            return false
        }
        if (!profileData.postalCode.isPinCodeValid()) {
            errorDescription.postValue("Please enter valid Postal Code")
            return false
        }
        if(!profileData.aadhar.isValidAadharNo()){
            errorDescription.postValue("Please enter valid Aadhar Number")
            return false
        }
        if(shouldCheckGST && !profileData.GSTNumber.isValidGST()){
            errorDescription.postValue("Please enter valid GST")
            return false
        }
        if (profileData.description.isStringEmpty()) {
            errorDescription.postValue("Please Describe about yourself")
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
                profileDataResponse.postValue(ApiResource.success(profileInfoRepository.getProfileData(userId)))
            } catch (exception: HttpException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateProfileData() {
        saveProfileResponse.value = ApiResource.loading(null)
        Log.d(TAG, "updateProfileData profile data: " + profileMutLiveData.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveProfileResponse.postValue(ApiResource.success(profileInfoRepository.updateProfile(profileMutLiveData.value?.userId.toString(), profileMutLiveData.value!!)))
            } catch (exception: HttpException) {
                saveProfileResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                saveProfileResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun uploadAadhar(file: File, trainerId: String) {
        uploadAadharResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = trainerId.toRequestBody("text/plain".toMediaType())
                val title = "aadhar".toRequestBody("text/plain".toMediaType())
                val requestFile: RequestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)
                uploadAadharResponse.postValue(ApiResource.success(profileInfoRepository.uploadAadharPdfFile(body, userId, title)))
            } catch (e: HttpException) {
                uploadAadharResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            }catch (e: IOException) {
                uploadAadharResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun uploadApn(file: File, trainerId: String) {
        uploadPanResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = trainerId.toRequestBody("text/plain".toMediaType())
                val title = "pan".toRequestBody("text/plain".toMediaType())
                val requestFile: RequestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)
                uploadPanResponse.postValue(ApiResource.success(profileInfoRepository.uploadPanPdfFile(body, userId, title)))
            } catch (e: HttpException) {
                uploadPanResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            }catch (e: IOException) {
                uploadPanResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }
}