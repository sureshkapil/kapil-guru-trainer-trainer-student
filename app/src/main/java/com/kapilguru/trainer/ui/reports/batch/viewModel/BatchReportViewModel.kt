package com.kapilguru.trainer.ui.reports.batch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApi
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import com.kapilguru.trainer.ui.reports.batch.BatchReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BatchReportViewModel(private val repository : BatchReportRepository) : ViewModel() {
    private val TAG = "BatchReportViewModel"
    var courseData : CourseResponse? = null
    val batchListResponse : MutableLiveData<ApiResource<BatchListApi>> = MutableLiveData()

    fun getBatchListResponse(batchListApiRequest: BatchListApiRequest){
        batchListResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                batchListResponse.postValue(ApiResource.success(data = repository.getBatchList(batchListApiRequest)))
            }catch (exception : HttpException){
                batchListResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception : IOException){
                batchListResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}