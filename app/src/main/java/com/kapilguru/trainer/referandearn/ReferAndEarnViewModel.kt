package com.kapilguru.trainer.referandearn

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MySubscriptionResponse
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.profile.data.SaveProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class ReferAndEarnViewModel(
    private val referAndEarnRepository: ReferAndEarnRepository,
    application: Application
) : ViewModel() {

    var uuid: MutableLiveData<String> = MutableLiveData()
    var inviteeEmail: MutableLiveData<String> = MutableLiveData()
    var inviteeContactNumber: MutableLiveData<String> = MutableLiveData()
    var resultDat: MutableLiveData<ApiResource<SaveProfileResponse>> = MutableLiveData()

    init {
        val generatedUUID: UUID = UUID.randomUUID()
        uuid.value = generatedUUID.toString()
    }

    fun requestApiCall(trainerId: Int, referralType: String) {
        val request = ReferAndEarnRequest().also { it ->
            it.referCode = uuid.value
            it.inviteeEmail = inviteeEmail.value
            it.inviteeContactNumber = inviteeContactNumber.value
            it.referralType = referralType
            it.userId = trainerId
        }
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(referAndEarnRepository.referAndEarn(request)))
            } catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }

        }
    }
}