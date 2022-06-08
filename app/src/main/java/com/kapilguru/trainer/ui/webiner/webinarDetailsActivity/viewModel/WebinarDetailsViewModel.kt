package com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.webiner.WebinarRepository
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData
import com.kapilguru.trainer.ui.webiner.model.WebinarResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WebinarDetailsViewModel(private val repository: WebinarRepository) : ViewModel() {
    private val TAG = "WebinarDetailsViewModel"

    var webinarData : ActiveWebinarData = ActiveWebinarData()
    var webinarResponseData : MutableLiveData<ApiResource<WebinarResponse>> = MutableLiveData()

  fun fetchWebinarDetails(webinarId: String){
      webinarResponseData.value = ApiResource.loading(data = null)
      viewModelScope.launch {
          try {
              webinarResponseData.postValue(ApiResource.success(repository.fetchWebinarDetails(webinarId)))
          } catch (e: HttpException) {
              webinarResponseData.postValue(ApiResource.error(data = null, message = e.code().toString()))
          }catch (e: IOException) {
              webinarResponseData.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
          }
      }
    }
}