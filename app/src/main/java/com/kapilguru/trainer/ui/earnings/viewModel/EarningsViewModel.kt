package com.kapilguru.trainer.ui.earnings.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.earnings.earningsDetails.EarningsDetailsResponse
import com.kapilguru.trainer.ui.earnings.model.EarningsDataResponse
import com.kapilguru.trainer.ui.earnings.model.EarningsDataResponseApi
import com.kapilguru.trainer.ui.earnings.model.EarningsListApiData
import com.kapilguru.trainer.ui.earnings.model.EarningsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class EarningsViewModel(private val earningsRepository: EarningsRepository,
              application: Application): AndroidViewModel(application) {

    var trainerId: Int
//    var earningsApiResponse: MutableLiveData<EarningsListApiData> = MutableLiveData()
    var earningsDataResponseApi: MutableLiveData<EarningsDataResponseApi> = MutableLiveData()

    var earningsDetailsResponse: MutableLiveData<ApiResource<EarningsDetailsResponse>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }

/*    var earningsListApi =  liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = earningsRepository.getearningList(trainerId.toString())))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }*/

    var earningsDataResponse = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = earningsRepository.getEarningData(trainerId.toString())))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun earningsDetailsResponseDetails() {
        earningsDetailsResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                earningsDetailsResponse.postValue(ApiResource.success(data = earningsRepository.getEarningDetails(trainerId.toString())))
            } catch (exception: HttpException) {
                earningsDetailsResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                earningsDetailsResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


   /* earningsApiResponse.value = ApiResource.loading(data = null)
    viewModelScope.launch {
        try {
            earningsApiResponse.postValue(
                ApiResource.success(
                    earningsRepository.getearningList(trainerId.toString())
                )
            )
        } catch (e: HttpException) {
            earningsApiResponse.postValue(
                ApiResource.error(
                    data = null,
                    message = e.code().toString() ?: "Error Occurred!"
                )
            )
        }
    }    */
}