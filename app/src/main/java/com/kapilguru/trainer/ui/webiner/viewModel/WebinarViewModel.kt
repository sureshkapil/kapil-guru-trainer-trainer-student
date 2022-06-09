package com.kapilguru.trainer.ui.webiner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.webiner.WebinarRepository
import com.kapilguru.trainer.ui.webiner.model.LiveUpComingWebinarResponse
import com.kapilguru.trainer.ui.webiner.model.WebinarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WebinarViewModel(private val webinarRepository: WebinarRepository, application: Application):AndroidViewModel(application){

    var totalWebinars: MutableLiveData<String> = MutableLiveData("0")
    var trainerId: Int
    var webinarId:MutableLiveData<String> = MutableLiveData()
    var webinarListApi : MutableLiveData<ApiResource<WebinarResponse>> = MutableLiveData()
    var liveUpComingWebinarListApi : MutableLiveData<ApiResource<LiveUpComingWebinarResponse>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }

    fun fetchWebinarList(){
        webinarListApi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webinarListApi.postValue(ApiResource.success(data = webinarRepository.getWebinarList(trainerId.toString())))
            } catch (exception: IOException) {
                webinarListApi.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: HttpException) {
                webinarListApi.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun fetchLiveUpComingWebinarList(){
        liveUpComingWebinarListApi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                liveUpComingWebinarListApi.postValue(ApiResource.success(data = webinarRepository.getLiveUpComingWebinarList(trainerId.toString())))
            } catch (exception: IOException) {
                liveUpComingWebinarListApi.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: HttpException) {
                liveUpComingWebinarListApi.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    var webinarDeleteApi = liveData( Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = webinarRepository.deleteWebinar(webinarId.value.toString())))
        }catch (exception:HttpException){
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception:IOException){
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}