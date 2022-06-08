package com.kapilguru.trainer.ui.courses.onGoingBatches.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.onGoingBatches.OnGoingBatchRepository
import com.kapilguru.trainer.ui.courses.onGoingBatches.models.OnGoingBatchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class OnGoingBatchViewModel(private val onGoingBatchRepository: OnGoingBatchRepository) :
    ViewModel() {


    var resultApiData: MutableLiveData<ApiResource<OnGoingBatchApi>> = MutableLiveData()


    fun fetchOnGoingBatchApi(trainerId: String) {

        resultApiData.value = ApiResource.loading(data = null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
               /* resultApiData.postValue(
                    ApiResource.success(
                        onGoingBatchRepository.getOnGoingBatchListInfo(
                            trainerId
                        )
                    )
                )*/
            } catch (exception: HttpException) {
                resultApiData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                resultApiData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }

        }
    }
}