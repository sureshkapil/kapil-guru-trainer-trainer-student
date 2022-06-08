package com.kapilguru.trainer.ui.profile.profileOrganisation.viewModel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.profile.data.ProfileData
import com.kapilguru.trainer.ui.profile.data.ProfileResponse
import com.kapilguru.trainer.ui.profile.data.SaveProfileResponse
import com.kapilguru.trainer.ui.profile.profileInfo.models.CityResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.CountryResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.StateResponce
import com.kapilguru.trainer.ui.profile.profileOrganisation.ProfileOrgRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileOrgViewModel(private val profileOrgRepository: ProfileOrgRepository) : ViewModel() {
    private val TAG = "ProfileOrgViewModel"
    val countryList: MutableLiveData<ApiResource<CountryResponce>> = MutableLiveData()
    val stateList: MutableLiveData<ApiResource<StateResponce>> = MutableLiveData()
    val cityList: MutableLiveData<ApiResource<CityResponce>> = MutableLiveData()
    var profileMutLiveData: MutableLiveData<ProfileData> = MutableLiveData()
    val saveProfileResponse : MutableLiveData<ApiResource<SaveProfileResponse>> = MutableLiveData()
    val errorDescription : MutableLiveData<String> = MutableLiveData()

    fun getIsOrganisation() : Boolean{
        return profileMutLiveData.value?.isOrganization ==1
    }

    fun getCountryList() {
        countryList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                countryList.postValue(ApiResource.success(profileOrgRepository.getCountryList()))
            } catch (exception: HttpException) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
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
                stateList.postValue(ApiResource.success(profileOrgRepository.getSateList(countryId)))
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
                cityList.postValue(ApiResource.success(profileOrgRepository.getCityList(stateId)))
            } catch (exception: HttpException) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun dataValid() : Boolean{
        val profileData = profileMutLiveData.value
        if(TextUtils.isEmpty(profileData?.orgContactNumber)){
            errorDescription.postValue("Please enter Contact Number")
            return false
        }
        if(TextUtils.isEmpty(profileData?.officialEmail)){
            errorDescription.postValue("Please enter Official Email")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgAddressLine1)){
            errorDescription.postValue("Please enter Address")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgCountryId)){
            errorDescription.postValue("Please select Country")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgStateId)){
            errorDescription.postValue("Please select State")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgCityId)){
            errorDescription.postValue("Please select City")
            return false
        }
        return true
    }

    fun saveProfileData() {
        saveProfileResponse.value = ApiResource.loading(null)
        Log.d(TAG, "saveProfileData profile data: "+profileMutLiveData.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveProfileResponse.postValue(ApiResource.success(profileOrgRepository.saveProfile(profileMutLiveData.value!!)))
            } catch (exception: HttpException) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun updateProfileData() {
        saveProfileResponse.value = ApiResource.loading(null)
        Log.d(TAG, "updateProfileData profile data: "+profileMutLiveData.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveProfileResponse.postValue(ApiResource.success(profileOrgRepository.updateProfile(
                    profileMutLiveData.value?.userId.toString(), profileMutLiveData.value!!)))
            } catch (exception: HttpException) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}