package com.kapilguru.trainer.splashScreen

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.profile.ProfileOptionsRepository
import com.kapilguru.trainer.ui.profile.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SplashScreenViewModel(private val profileOptionsRepository: ProfileOptionsRepository) :
    ViewModel() {
    private val TAG: String = "ProfileOptionsViewModel"
    var profileDataResponse: MutableLiveData<ApiResource<ProfileResponse>> = MutableLiveData()


    fun getProfileData(userId: String) {
        profileDataResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileDataResponse.postValue(ApiResource.success(profileOptionsRepository.getProfileData(userId)))
            } catch (exception: HttpException) {
                Log.d(TAG, "getProfileData: ${exception.code()}")
                profileDataResponse.postValue(ApiResource.error(code = exception.code(),data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                Log.d(TAG, "getProfileData: ${exception.toString()}")
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}