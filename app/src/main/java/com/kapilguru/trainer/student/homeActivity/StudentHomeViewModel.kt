package com.kapilguru.trainer.student.homeActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest
import com.kapilguru.trainer.ui.changePassword.model.LogoutResponse
import com.kapilguru.trainer.ui.home.NotificationCountResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentHomeViewModel(private val homeRepository: StudentHomeRepository, val context: Application) : AndroidViewModel(context) {
    val logoutResponse: MutableLiveData<ApiResource<LogoutResponse>> = MutableLiveData()
    val notificationCountResponse: MutableLiveData<ApiResource<NotificationCountResponse>> = MutableLiveData()

    fun logoutUser(logoutRequest: LogoutRequest) {
        logoutResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logoutResponse.postValue(ApiResource.success(data = homeRepository.logoutUser(logoutRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code))
            } catch (exception: IOException) {
                logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
        fun notificationCountApiCall() {
            notificationCountResponse.value = ApiResource.loading(data = null)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    notificationCountResponse.postValue(ApiResource.success(data = homeRepository.getNotificationCount(StorePreferences(context).studentId.toString())))
                } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code))
                } catch (exception: IOException) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (exception: HttpException) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (exception: RuntimeException) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }
            }
        }
    }